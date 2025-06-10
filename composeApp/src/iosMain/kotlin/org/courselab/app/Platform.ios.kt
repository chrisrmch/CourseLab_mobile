package org.courselab.app


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureDevicePositionBack
import platform.AVFoundation.AVCapturePhoto
import platform.AVFoundation.AVCapturePhotoOutput
import platform.AVFoundation.AVCapturePhotoCaptureDelegateProtocol
import platform.AVFoundation.AVCapturePhotoQualityPrioritizationQuality
import platform.AVFoundation.AVCapturePhotoSettings
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureSessionPresetPhoto
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.AVVideoCodecJPEG
import platform.AVFoundation.AVVideoCodecKey
import platform.AVFoundation.position
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIDevice
import platform.UIKit.UIScreen
import platform.UIKit.UIView
import platform.darwin.NSObject
import platform.Foundation.NSError
import platform.posix.`true`
import kotlin.native.internal.collectReferenceFieldValues

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()


@OptIn(ExperimentalForeignApi::class)
actual fun screenDetails(): ScreenDetails {
    val screen = UIScreen.mainScreen
    val bounds = screen.bounds
    val scale = screen.scale

    val widthPoints = bounds.useContents { size.width.toInt() }
    val heightPoints = bounds.useContents { size.height.toInt() }

    val density = scale.toFloat()

    return ScreenDetails(
        density = density,
        densityDpi = (density * 160f).toInt(),
        scaledDensity = density,
        widthDp = widthPoints.toFloat(),
        heightDp = heightPoints.toFloat(),
    )
}

@OptIn(ExperimentalForeignApi::class,  ExperimentalMaterial3Api::class)
@Composable
actual fun CameraView(modifier: Modifier) {

    val avCapturePhotoOutput = AVCapturePhotoOutput()

    // -------- 1. Configuración de cámara -----------
    val cameraSession = remember {
        // Dispositivo back-camera
        val device = AVCaptureDevice.devicesWithMediaType(AVMediaTypeVideo)
            .firstNotNullOf { it as AVCaptureDevice? }
            .takeIf { it.position == AVCaptureDevicePositionBack }
            ?: error("No back camera")

        val input  = AVCaptureDeviceInput.deviceInputWithDevice(device, null) as AVCaptureDeviceInput
        val output = avCapturePhotoOutput.apply {
            // Foto de > 12 Mpx, HDR, RAW, etc. funcionan sólo si dejas esto en true
            highResolutionCaptureEnabled = true       // ✅
            livePhotoCaptureEnabled = true
            appleProRAWEnabled = true
            maxPhotoQualityPrioritization = AVCapturePhotoQualityPrioritizationQuality
        }

        AVCaptureSession().apply {
            sessionPreset = AVCaptureSessionPresetPhoto
            addInput(input)
            addOutput(output)
        }
    }


    val output = remember {
        avCapturePhotoOutput.apply {
            highResolutionCaptureEnabled = true
        }.also { cameraSession.addOutput(it) }
    }

    // --- para manejar la foto capturada ---
    val photoCaptureDelegate = remember {
        object : NSObject(), AVCapturePhotoCaptureDelegateProtocol {
            override fun captureOutput(output: AVCapturePhotoOutput, didFinishProcessingPhoto: AVCapturePhoto, error: NSError?) {
                if (error != null) {
                    println("Error capturing photo: ${error.localizedDescription}")
                    return
                }
                // Aquí puedes procesar `didFinishProcessingPhoto.fileDataRepresentation()`
                println("Photo captured successfully!")
            }
        }
    }
    // --- cuando el usuario pulse el disparo ---
    fun takePhoto() {
        val settings = AVCapturePhotoSettings.photoSettings()
        output.highResolutionCaptureEnabled = true
        settings.highResolutionPhotoEnabled = true
        output.capturePhotoWithSettings(settings, delegate = photoCaptureDelegate)
    }

    val previewLayer = remember { AVCaptureVideoPreviewLayer(session = cameraSession).apply {
        videoGravity = AVLayerVideoGravityResizeAspectFill
    }}

    // -------- 2. UIKit interop -----------
    UIKitView(
        factory = {
            UIView(frame = CGRectZero.readValue()).apply {
                layer.addSublayer(previewLayer)
                cameraSession.startRunning()
            }
        },
        modifier = modifier.fillMaxSize(),

        // Se llama en cada recomposición (aqua sólo reajustamos el frame)

        update = { view -> previewLayer.frame = view.bounds },

        // Liberamos recursos cuando el composable sale de la jerarquía

        onRelease = { cameraSession.stopRunning() },

        // No queremos que consuma toques: falso => deja pasar los eventos

        properties = UIKitInteropProperties(
            isInteractive = false,
            isNativeAccessibilityEnabled = false
        )
    )
}