// File internal version: 0.1.0
package com.goreecloud.camera.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.Surface
import android.view.TextureView
import java.util.concurrent.Executor

enum class CameraSessionState {
    IDLE,
    WAITING_FOR_SURFACE,
    OPENING,
    PREVIEWING,
    ERROR,
}

class CameraSessionController(
    context: Context,
    private val textureView: TextureView,
    private val onStateChanged: (CameraSessionState, String?) -> Unit,
) {
    private val appContext = context.applicationContext
    private val cameraManager = appContext.getSystemService(CameraManager::class.java)
    private val capabilityRegistry = CameraCapabilityRegistry(cameraManager)
    private val mainHandler = Handler(Looper.getMainLooper())
    private val cameraThread = HandlerThread("GoreeCloudCameraSession").apply { start() }
    private val cameraHandler = Handler(cameraThread.looper)
    private val cameraExecutor = Executor { command -> cameraHandler.post(command) }

    @Volatile
    private var desiredActive = false

    @Volatile
    private var opening = false

    private var cameraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null
    private var previewSurface: Surface? = null

    fun start() {
        desiredActive = true
        openIfReady()
    }

    @Synchronized
    fun stop() {
        desiredActive = false
        closeSessionResources()
        transition(CameraSessionState.IDLE, null)
    }

    fun shutdown() {
        stop()
        cameraThread.quitSafely()
        if (Thread.currentThread() !== cameraThread) {
            runCatching { cameraThread.join(1_000) }
        }
    }

    @Synchronized
    private fun openIfReady() {
        if (!desiredActive) return
        if (cameraDevice != null || opening) return

        if (appContext.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            transition(CameraSessionState.ERROR, "Camera permission is not granted")
            return
        }

        if (!textureView.isAvailable) {
            transition(CameraSessionState.WAITING_FOR_SURFACE, null)
            return
        }

        val profiles = capabilityRegistry.profiles()
        val selected = CameraSelector.selectDefaultCamera(profiles.map { it.descriptor })
        if (selected == null) {
            transition(CameraSessionState.ERROR, "No camera is available")
            return
        }

        val profile = profiles.firstOrNull { it.descriptor.id == selected.id }
        val previewSize = profile?.let {
            capabilityRegistry.selectPreviewSize(it, textureView.width, textureView.height)
        }
        val surfaceTexture = textureView.surfaceTexture
        if (profile == null || previewSize == null || surfaceTexture == null) {
            transition(CameraSessionState.ERROR, "No compatible preview configuration is available")
            return
        }

        surfaceTexture.setDefaultBufferSize(previewSize.width, previewSize.height)
        previewSurface?.release()
        previewSurface = Surface(surfaceTexture)
        opening = true
        transition(
            CameraSessionState.OPENING,
            "camera=${selected.id}, preview=${previewSize.width}x${previewSize.height}",
        )
        openCamera(selected.id)
    }

    @SuppressLint("MissingPermission")
    private fun openCamera(cameraId: String) {
        try {
            cameraManager.openCamera(
                cameraId,
                cameraExecutor,
                object : CameraDevice.StateCallback() {
                    override fun onOpened(camera: CameraDevice) {
                        synchronized(this@CameraSessionController) {
                            opening = false
                            if (!desiredActive) {
                                camera.close()
                                return
                            }
                            cameraDevice = camera
                        }
                        createPreviewSession(camera)
                    }

                    override fun onDisconnected(camera: CameraDevice) {
                        synchronized(this@CameraSessionController) {
                            opening = false
                            if (cameraDevice === camera) cameraDevice = null
                            camera.close()
                        }
                        transition(CameraSessionState.ERROR, "Camera disconnected")
                    }

                    override fun onError(camera: CameraDevice, error: Int) {
                        synchronized(this@CameraSessionController) {
                            opening = false
                            if (cameraDevice === camera) cameraDevice = null
                            camera.close()
                        }
                        transition(CameraSessionState.ERROR, "Camera error code $error")
                    }
                },
            )
        } catch (securityException: SecurityException) {
            opening = false
            transition(CameraSessionState.ERROR, "Camera permission changed while opening")
        } catch (exception: Exception) {
            opening = false
            transition(CameraSessionState.ERROR, exception.message ?: "Unable to open camera")
        }
    }

    private fun createPreviewSession(camera: CameraDevice) {
        val surface = synchronized(this) { previewSurface }
        if (!desiredActive || surface == null) {
            camera.close()
            return
        }

        val outputConfiguration = OutputConfiguration(surface)
        val sessionConfiguration = SessionConfiguration(
            SessionConfiguration.SESSION_REGULAR,
            listOf(outputConfiguration),
            cameraExecutor,
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    if (!desiredActive) {
                        session.close()
                        return
                    }

                    val request = try {
                        camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).apply {
                            addTarget(surface)
                            set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
                        }.build()
                    } catch (exception: Exception) {
                        session.close()
                        transition(CameraSessionState.ERROR, exception.message ?: "Preview request failed")
                        return
                    }

                    synchronized(this@CameraSessionController) {
                        captureSession?.close()
                        captureSession = session
                    }

                    try {
                        session.setRepeatingRequest(request, null, cameraHandler)
                        transition(CameraSessionState.PREVIEWING, null)
                    } catch (exception: Exception) {
                        session.close()
                        synchronized(this@CameraSessionController) {
                            if (captureSession === session) captureSession = null
                        }
                        transition(CameraSessionState.ERROR, exception.message ?: "Preview could not start")
                    }
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    session.close()
                    transition(CameraSessionState.ERROR, "Camera preview session configuration failed")
                }
            },
        )

        try {
            camera.createCaptureSession(sessionConfiguration)
        } catch (exception: Exception) {
            transition(CameraSessionState.ERROR, exception.message ?: "Preview session could not be created")
        }
    }

    @Synchronized
    private fun closeSessionResources() {
        captureSession?.close()
        captureSession = null
        cameraDevice?.close()
        cameraDevice = null
        previewSurface?.release()
        previewSurface = null
    }

    private fun transition(state: CameraSessionState, detail: String?) {
        mainHandler.post { onStateChanged(state, detail) }
    }
}
