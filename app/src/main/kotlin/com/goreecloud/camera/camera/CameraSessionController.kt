// File internal version: 0.2.0
package com.goreecloud.camera.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureFailure
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.media.ImageReader
import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.Surface
import android.view.TextureView
import com.goreecloud.camera.storage.PendingPhoto
import com.goreecloud.camera.storage.PhotoMediaStoreCommitter
import java.util.concurrent.Executor

enum class CameraSessionState {
    IDLE,
    WAITING_FOR_SURFACE,
    OPENING,
    PREVIEWING,
    CAPTURING,
    ERROR,
}

data class PhotoCaptureOutcome(
    val displayName: String? = null,
    val uri: Uri? = null,
    val errorMessage: String? = null,
) {
    val isSuccess: Boolean
        get() = uri != null && errorMessage == null
}

class CameraSessionController(
    context: Context,
    private val textureView: TextureView,
    private val onStateChanged: (CameraSessionState, String?) -> Unit,
    private val onPhotoCaptureFinished: (PhotoCaptureOutcome) -> Unit = {},
) {
    private val appContext = context.applicationContext
    private val cameraManager = appContext.getSystemService(CameraManager::class.java)
    private val capabilityRegistry = CameraCapabilityRegistry(cameraManager)
    private val photoCommitter = PhotoMediaStoreCommitter(appContext.contentResolver)
    private val mainHandler = Handler(Looper.getMainLooper())
    private val cameraThread = HandlerThread("GoreeCloudCameraSession").apply { start() }
    private val cameraHandler = Handler(cameraThread.looper)
    private val cameraExecutor = Executor { command -> cameraHandler.post(command) }

    @Volatile
    private var desiredActive = false

    @Volatile
    private var opening = false

    @Volatile
    private var sessionState = CameraSessionState.IDLE

    private var cameraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null
    private var previewSurface: Surface? = null
    private var imageReader: ImageReader? = null
    private var pendingPhoto: PendingPhoto? = null
    private var captureInFlight = false

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
    fun capturePhoto() {
        if (!desiredActive || sessionState != CameraSessionState.PREVIEWING) {
            notifyPhotoOutcome(
                PhotoCaptureOutcome(errorMessage = "Camera preview is not ready for capture"),
            )
            return
        }
        if (captureInFlight) {
            notifyPhotoOutcome(
                PhotoCaptureOutcome(errorMessage = "A photo capture is already in progress"),
            )
            return
        }

        captureInFlight = true
        transition(CameraSessionState.CAPTURING, null)
        cameraHandler.post(::captureStillImage)
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
        val jpegSize = profile?.let(capabilityRegistry::selectJpegSize)
        val surfaceTexture = textureView.surfaceTexture
        if (profile == null || previewSize == null || jpegSize == null || surfaceTexture == null) {
            transition(CameraSessionState.ERROR, "No compatible preview and JPEG configuration is available")
            return
        }

        surfaceTexture.setDefaultBufferSize(previewSize.width, previewSize.height)
        previewSurface?.release()
        previewSurface = Surface(surfaceTexture)

        imageReader?.close()
        imageReader = ImageReader.newInstance(
            jpegSize.width,
            jpegSize.height,
            ImageFormat.JPEG,
            MAX_IMAGES,
        ).apply {
            setOnImageAvailableListener({ reader -> handlePhotoImageAvailable(reader) }, cameraHandler)
        }

        opening = true
        transition(
            CameraSessionState.OPENING,
            "camera=${selected.id}, preview=${previewSize.width}x${previewSize.height}, jpeg=${jpegSize.width}x${jpegSize.height}",
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
                        createCaptureSession(camera)
                    }

                    override fun onDisconnected(camera: CameraDevice) {
                        synchronized(this@CameraSessionController) {
                            opening = false
                            if (cameraDevice === camera) cameraDevice = null
                            camera.close()
                        }
                        failPendingPhoto("Camera disconnected")
                        transition(CameraSessionState.ERROR, "Camera disconnected")
                    }

                    override fun onError(camera: CameraDevice, error: Int) {
                        synchronized(this@CameraSessionController) {
                            opening = false
                            if (cameraDevice === camera) cameraDevice = null
                            camera.close()
                        }
                        failPendingPhoto("Camera error code $error")
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

    private fun createCaptureSession(camera: CameraDevice) {
        val preview = synchronized(this) { previewSurface }
        val photoSurface = synchronized(this) { imageReader?.surface }
        if (!desiredActive || preview == null || photoSurface == null) {
            camera.close()
            return
        }

        val sessionConfiguration = SessionConfiguration(
            SessionConfiguration.SESSION_REGULAR,
            listOf(OutputConfiguration(preview), OutputConfiguration(photoSurface)),
            cameraExecutor,
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    if (!desiredActive) {
                        session.close()
                        return
                    }

                    val request = try {
                        camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).apply {
                            addTarget(preview)
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
                    transition(CameraSessionState.ERROR, "Camera capture session configuration failed")
                }
            },
        )

        try {
            camera.createCaptureSession(sessionConfiguration)
        } catch (exception: Exception) {
            transition(CameraSessionState.ERROR, exception.message ?: "Capture session could not be created")
        }
    }

    private fun captureStillImage() {
        val camera: CameraDevice
        val session: CameraCaptureSession
        val photoSurface: Surface

        synchronized(this) {
            camera = cameraDevice ?: return finishPhotoFailure("Camera device is unavailable")
            session = captureSession ?: return finishPhotoFailure("Camera session is unavailable")
            photoSurface = imageReader?.surface ?: return finishPhotoFailure("JPEG output is unavailable")
        }

        val reserved = try {
            photoCommitter.reserve()
        } catch (exception: Exception) {
            finishPhotoFailure(exception.message ?: "Unable to reserve photo storage")
            return
        }

        synchronized(this) {
            if (!desiredActive || !captureInFlight) {
                photoCommitter.discard(reserved)
                return
            }
            pendingPhoto = reserved
        }

        val request = try {
            camera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE).apply {
                addTarget(photoSurface)
                set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
            }.build()
        } catch (exception: Exception) {
            failPendingPhoto(exception.message ?: "Unable to create still-capture request")
            return
        }

        try {
            session.capture(
                request,
                object : CameraCaptureSession.CaptureCallback() {
                    override fun onCaptureFailed(
                        session: CameraCaptureSession,
                        request: CaptureRequest,
                        failure: CaptureFailure,
                    ) {
                        failPendingPhoto("Still capture failed with reason ${failure.reason}")
                    }
                },
                cameraHandler,
            )
        } catch (exception: Exception) {
            failPendingPhoto(exception.message ?: "Unable to submit still-capture request")
        }
    }

    private fun handlePhotoImageAvailable(reader: ImageReader) {
        val image = runCatching { reader.acquireNextImage() }.getOrNull() ?: return
        try {
            val plane = image.planes.firstOrNull()
            if (plane == null) {
                failPendingPhoto("JPEG image contained no readable plane")
                return
            }

            val buffer = plane.buffer
            val jpegBytes = ByteArray(buffer.remaining())
            buffer.get(jpegBytes)

            val reserved = synchronized(this) {
                val photo = pendingPhoto
                pendingPhoto = null
                photo
            } ?: return

            val uri = try {
                photoCommitter.commit(reserved, jpegBytes)
            } catch (exception: Exception) {
                finishPhotoFailure(exception.message ?: "Unable to finalize photo")
                return
            }

            val activeAfterCapture = synchronized(this) {
                captureInFlight = false
                desiredActive
            }
            transition(
                if (activeAfterCapture) CameraSessionState.PREVIEWING else CameraSessionState.IDLE,
                null,
            )
            notifyPhotoOutcome(
                PhotoCaptureOutcome(
                    displayName = reserved.displayName,
                    uri = uri,
                ),
            )
        } finally {
            image.close()
        }
    }

    private fun failPendingPhoto(message: String) {
        val reserved = synchronized(this) {
            val photo = pendingPhoto
            pendingPhoto = null
            photo
        }
        if (reserved != null) {
            photoCommitter.discard(reserved)
        }
        finishPhotoFailure(message)
    }

    private fun finishPhotoFailure(message: String) {
        synchronized(this) {
            captureInFlight = false
        }
        if (desiredActive && sessionState != CameraSessionState.ERROR) {
            transition(CameraSessionState.PREVIEWING, null)
        }
        notifyPhotoOutcome(PhotoCaptureOutcome(errorMessage = message))
    }

    @Synchronized
    private fun closeSessionResources() {
        pendingPhoto?.let(photoCommitter::discard)
        pendingPhoto = null
        captureInFlight = false

        captureSession?.close()
        captureSession = null
        cameraDevice?.close()
        cameraDevice = null
        previewSurface?.release()
        previewSurface = null
        imageReader?.close()
        imageReader = null
    }

    private fun transition(state: CameraSessionState, detail: String?) {
        sessionState = state
        mainHandler.post { onStateChanged(state, detail) }
    }

    private fun notifyPhotoOutcome(outcome: PhotoCaptureOutcome) {
        mainHandler.post { onPhotoCaptureFinished(outcome) }
    }

    private companion object {
        const val MAX_IMAGES = 2
    }
}
