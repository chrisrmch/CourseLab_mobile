package org.courselab.app


import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()


actual fun screenDetails(): ScreenDetails {
    val metrics = Resources.getSystem().displayMetrics

    val density = metrics.density
    val densityDpi = metrics.densityDpi
    val scaledDensityPxPerSp = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        1f,
        metrics
    )

    val widthPx = metrics.widthPixels
    val heightPx = metrics.heightPixels

    val widthDp = widthPx.toFloat() / density
    val heightDp = heightPx.toFloat() / density

    return ScreenDetails(
        density = density,
        densityDpi = densityDpi,
        scaledDensity = scaledDensityPxPerSp,
        widthDp = widthDp,
        heightDp = heightDp
    )
}

@Composable
actual fun CameraView(modifier: Modifier) {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }

    AndroidView(
        factory = { previewView },
        modifier = modifier.fillMaxSize(),
        update = {
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            preview.surfaceProvider = previewView.surfaceProvider
            val cameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
            cameraProvider.bindToLifecycle(lifeCycleOwner, cameraSelector, preview)
        })
}