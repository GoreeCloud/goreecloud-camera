// File internal version: 0.1.0
package com.goreecloud.camera.camera

import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.util.Size
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class CameraProfile(
    val descriptor: CameraDescriptor,
    val hardwareLevel: Int,
    val supportsRaw: Boolean,
    val supportsLogicalMultiCamera: Boolean,
    val previewSizes: List<Size>,
)

class CameraCapabilityRegistry(
    private val cameraManager: CameraManager,
) {
    fun profiles(): List<CameraProfile> = try {
        cameraManager.cameraIdList.mapNotNull(::readProfile)
    } catch (_: CameraAccessException) {
        emptyList()
    }

    fun selectPreviewSize(profile: CameraProfile, viewWidth: Int, viewHeight: Int): Size? {
        if (profile.previewSizes.isEmpty()) return null

        val targetLong = max(viewWidth, viewHeight).coerceAtLeast(1)
        val targetShort = min(viewWidth, viewHeight).coerceAtLeast(1)
        val targetRatio = targetLong.toDouble() / targetShort.toDouble()
        val targetArea = targetLong.toLong() * targetShort.toLong()

        val bounded = profile.previewSizes.filter { size ->
            val longSide = max(size.width, size.height)
            val shortSide = min(size.width, size.height)
            longSide <= 1920 && shortSide <= 1080
        }
        val candidates = bounded.ifEmpty { profile.previewSizes }

        return candidates.minWithOrNull(
            compareBy<Size>(
                { size ->
                    val longSide = max(size.width, size.height)
                    val shortSide = min(size.width, size.height).coerceAtLeast(1)
                    abs((longSide.toDouble() / shortSide.toDouble()) - targetRatio)
                },
                { size -> abs((size.width.toLong() * size.height.toLong()) - targetArea) },
            ),
        )
    }

    private fun readProfile(cameraId: String): CameraProfile? {
        val characteristics = try {
            cameraManager.getCameraCharacteristics(cameraId)
        } catch (_: CameraAccessException) {
            return null
        }

        val lensFacing = when (characteristics.get(CameraCharacteristics.LENS_FACING)) {
            CameraCharacteristics.LENS_FACING_BACK -> LensFacing.BACK
            CameraCharacteristics.LENS_FACING_FRONT -> LensFacing.FRONT
            CameraCharacteristics.LENS_FACING_EXTERNAL -> LensFacing.EXTERNAL
            else -> LensFacing.UNKNOWN
        }

        val capabilities = characteristics
            .get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
            ?.toSet()
            .orEmpty()

        val previewSizes = characteristics
            .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            ?.getOutputSizes(SurfaceTexture::class.java)
            ?.toList()
            .orEmpty()

        return CameraProfile(
            descriptor = CameraDescriptor(cameraId, lensFacing),
            hardwareLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL) ?: -1,
            supportsRaw = CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_RAW in capabilities,
            supportsLogicalMultiCamera = CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_LOGICAL_MULTI_CAMERA in capabilities,
            previewSizes = previewSizes,
        )
    }
}
