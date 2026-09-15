// File internal version: 0.1.0
package com.goreecloud.camera

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.view.Gravity
import android.view.TextureView
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.goreecloud.camera.camera.CameraCapabilityRegistry
import com.goreecloud.camera.camera.CameraSessionController
import com.goreecloud.camera.camera.CameraSessionState
import android.hardware.camera2.CameraManager

class MainActivity : Activity() {
    private lateinit var previewView: TextureView
    private lateinit var stateLabel: TextView
    private lateinit var capabilityLabel: TextView
    private lateinit var permissionButton: Button
    private lateinit var sessionController: CameraSessionController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildInterface()

        sessionController = CameraSessionController(this, previewView) { state, detail ->
            val stateText = getString(R.string.session_status, state.name.lowercase())
            stateLabel.text = if (detail.isNullOrBlank()) stateText else "$stateText\n$detail"
        }

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

        refreshCapabilities()
        renderPermissionState()
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
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            renderPermissionState()
            refreshCapabilities()
            maybeStartPreview()
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

        setContentView(root)
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
    }
}
