// File internal version: 0.4.0
package com.goreecloud.camera

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.TextureView
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowInsets
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.goreecloud.camera.camera.CameraCapabilityRegistry
import com.goreecloud.camera.camera.CameraSessionController
import com.goreecloud.camera.camera.CameraSessionState

class MainActivity : Activity() {
    private lateinit var previewView: TextureView
    private lateinit var stateLabel: TextView
    private lateinit var capabilityLabel: TextView
    private lateinit var photoStatusLabel: TextView
    private lateinit var videoStatusLabel: TextView
    private lateinit var permissionButton: Button
    private lateinit var shutterButton: Button
    private lateinit var videoButton: Button
    private lateinit var sessionController: CameraSessionController

    private var currentSessionState = CameraSessionState.IDLE
    private var videoCapabilityAvailable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildInterface()

        sessionController = CameraSessionController(
            context = this,
            textureView = previewView,
            onStateChanged = { state, detail ->
                currentSessionState = state
                val stateText = getString(R.string.session_status, state.name.lowercase())
                stateLabel.text = if (detail.isNullOrBlank()) stateText else "$stateText\n$detail"

                when (state) {
                    CameraSessionState.STARTING_VIDEO -> {
                        videoStatusLabel.text = getString(R.string.video_starting)
                    }
                    CameraSessionState.RECORDING -> {
                        videoStatusLabel.text = getString(R.string.video_recording)
                    }
                    CameraSessionState.STOPPING_VIDEO -> {
                        videoStatusLabel.text = getString(R.string.video_stopping)
                    }
                    else -> Unit
                }
                updateCaptureControls()
            },
            onPhotoCaptureFinished = { outcome ->
                photoStatusLabel.text = if (outcome.isSuccess) {
                    getString(R.string.photo_saved, outcome.displayName.orEmpty())
                } else {
                    getString(R.string.photo_failed, outcome.errorMessage.orEmpty())
                }
            },
            onVideoCapabilityChanged = { available ->
                videoCapabilityAvailable = available
                videoStatusLabel.text = when {
                    !available -> getString(R.string.video_status_unavailable)
                    checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED ->
                        getString(R.string.video_status_ready)
                    else -> getString(R.string.video_status_needs_microphone)
                }
                updateCaptureControls()
            },
            onVideoRecordingFinished = { outcome ->
                videoStatusLabel.text = if (outcome.isSuccess) {
                    getString(R.string.video_saved, outcome.displayName.orEmpty())
                } else {
                    getString(R.string.video_failed, outcome.errorMessage.orEmpty())
                }
                updateCaptureControls()
            },
        )

        previewView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                maybeStartPreview()
            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) = Unit

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                sessionController.stop()
                return true
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) = Unit
        }

        permissionButton.setOnClickListener {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }

        shutterButton.setOnClickListener {
            photoStatusLabel.text = getString(R.string.photo_capture_in_progress)
            sessionController.capturePhoto()
        }

        videoButton.setOnClickListener {
            when (currentSessionState) {
                CameraSessionState.RECORDING -> {
                    videoStatusLabel.text = getString(R.string.video_stopping)
                    sessionController.stopVideoRecording()
                }
                CameraSessionState.PREVIEWING -> beginVideoRecordingFromUserAction()
                else -> Unit
            }
        }

        refreshCapabilities()
        renderPermissionState()
        updateCaptureControls()
    }

    override fun onResume() {
        super.onResume()
        maybeStartPreview()
    }

    override fun onPause() {
        sessionController.stop()
        super.onPause()
    }

    override fun onDestroy() {
        sessionController.shutdown()
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                renderPermissionState()
                refreshCapabilities()
                maybeStartPreview()
            }
            REQUEST_RECORD_AUDIO_PERMISSION -> {
                val granted = checkSelfPermission(Manifest.permission.RECORD_AUDIO) ==
                    PackageManager.PERMISSION_GRANTED
                videoStatusLabel.text = if (granted) {
                    getString(R.string.video_status_permission_granted)
                } else {
                    getString(R.string.video_status_permission_denied)
                }
                updateCaptureControls()
            }
        }
    }

    private fun buildInterface() {
        val root = FrameLayout(this).apply {
            setBackgroundColor(Color.BLACK)
        }

        previewView = TextureView(this).apply {
            contentDescription = getString(R.string.preview_content_description)
        }
        root.addView(previewView, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))

        val topPanel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(12), dp(16), dp(12))
            setBackgroundColor(Color.argb(168, 0, 0, 0))
        }

        val title = TextView(this).apply {
            text = getString(R.string.engineering_shell)
            setTextColor(Color.WHITE)
            textSize = 15f
        }
        stateLabel = TextView(this).apply {
            text = getString(R.string.session_status, CameraSessionState.IDLE.name.lowercase())
            setTextColor(Color.LTGRAY)
            textSize = 13f
            setPadding(0, dp(6), 0, 0)
        }
        capabilityLabel = TextView(this).apply {
            setTextColor(Color.LTGRAY)
            textSize = 13f
            setPadding(0, dp(4), 0, 0)
        }
        topPanel.addView(title, LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        topPanel.addView(stateLabel, LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        topPanel.addView(capabilityLabel, LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT))

        root.addView(
            topPanel,
            FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, Gravity.TOP),
        )

        permissionButton = Button(this).apply {
            text = getString(R.string.grant_camera_permission)
            visibility = View.GONE
        }
        root.addView(
            permissionButton,
            FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER),
        )

        val capturePaddingHorizontal = dp(16)
        val capturePaddingTop = dp(8)
        val capturePaddingBottom = dp(16)
        val capturePanel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(
                capturePaddingHorizontal,
                capturePaddingTop,
                capturePaddingHorizontal,
                capturePaddingBottom,
            )
            setBackgroundColor(Color.argb(168, 0, 0, 0))
        }

        photoStatusLabel = TextView(this).apply {
            text = getString(R.string.photo_status_ready)
            setTextColor(Color.WHITE)
            textSize = 13f
            gravity = Gravity.CENTER
        }
        shutterButton = Button(this).apply {
            text = getString(R.string.capture_photo)
            contentDescription = getString(R.string.capture_photo_content_description)
            isEnabled = false
        }
        videoStatusLabel = TextView(this).apply {
            text = getString(R.string.video_status_checking)
            setTextColor(Color.WHITE)
            textSize = 13f
            gravity = Gravity.CENTER
            setPadding(0, dp(8), 0, 0)
        }
        videoButton = Button(this).apply {
            text = getString(R.string.record_video)
            contentDescription = getString(R.string.record_video_content_description)
            isEnabled = false
        }

        capturePanel.addView(photoStatusLabel, LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        capturePanel.addView(shutterButton, LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT))
        capturePanel.addView(videoStatusLabel, LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        capturePanel.addView(videoButton, LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT))

        root.addView(
            capturePanel,
            FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, Gravity.BOTTOM),
        )

        root.setOnApplyWindowInsetsListener { _, insets ->
            val bottomSystemInset = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insets.getInsets(WindowInsets.Type.systemBars()).bottom
            } else {
                @Suppress("DEPRECATION")
                insets.systemWindowInsetBottom
            }
            capturePanel.setPadding(
                capturePaddingHorizontal,
                capturePaddingTop,
                capturePaddingHorizontal,
                capturePaddingBottom + bottomSystemInset,
            )
            insets
        }
        root.requestApplyInsets()

        setContentView(root)
    }

    private fun beginVideoRecordingFromUserAction() {
        if (!videoCapabilityAvailable) {
            videoStatusLabel.text = getString(R.string.video_status_unavailable)
            return
        }

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            videoStatusLabel.text = getString(R.string.video_status_needs_microphone)
            requestPermissions(
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO_PERMISSION,
            )
            return
        }

        videoStatusLabel.text = getString(R.string.video_starting)
        sessionController.startVideoRecording()
    }

    private fun maybeStartPreview() {
        if (!::sessionController.isInitialized) return
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            permissionButton.visibility = View.GONE
            sessionController.start()
        } else {
            sessionController.stop()
            renderPermissionState()
        }
    }

    private fun renderPermissionState() {
        val granted = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        permissionButton.visibility = if (granted) View.GONE else View.VISIBLE
        if (!granted) {
            stateLabel.text = getString(R.string.permission_required)
        }
        updateCaptureControls()
    }

    private fun updateCaptureControls() {
        val cameraGranted = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        shutterButton.isEnabled = cameraGranted && currentSessionState == CameraSessionState.PREVIEWING

        when (currentSessionState) {
            CameraSessionState.RECORDING -> {
                videoButton.isEnabled = true
                videoButton.text = getString(R.string.stop_video)
                videoButton.contentDescription = getString(R.string.stop_video_content_description)
            }
            CameraSessionState.PREVIEWING -> {
                videoButton.isEnabled = cameraGranted && videoCapabilityAvailable
                videoButton.text = getString(R.string.record_video)
                videoButton.contentDescription = getString(R.string.record_video_content_description)
            }
            else -> {
                videoButton.isEnabled = false
                videoButton.text = getString(R.string.record_video)
                videoButton.contentDescription = getString(R.string.record_video_content_description)
            }
        }
    }

    private fun refreshCapabilities() {
        val manager = getSystemService(CameraManager::class.java)
        val profiles = CameraCapabilityRegistry(manager).profiles()
        capabilityLabel.text = if (profiles.isEmpty()) {
            getString(R.string.camera_unavailable)
        } else {
            getString(R.string.capability_summary, profiles.size)
        }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private companion object {
        const val REQUEST_CAMERA_PERMISSION = 1001
        const val REQUEST_RECORD_AUDIO_PERMISSION = 1002
    }
}
