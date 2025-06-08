package org.courselab.app


import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.TypedValue

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()


actual fun screenDetails(): ScreenDetails {
    val metrics = Resources.getSystem().displayMetrics

    val density       = metrics.density
    val densityDpi    = metrics.densityDpi
    val scaledDensityPxPerSp = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        1f,
        metrics
    )

    val widthPx  = metrics.widthPixels
    val heightPx = metrics.heightPixels

    val widthDp  = widthPx.toFloat() / density
    val heightDp = heightPx.toFloat() / density

    return ScreenDetails(
        density       = density,
        densityDpi    = densityDpi,
        scaledDensity = scaledDensityPxPerSp,
        widthDp       = widthDp,
        heightDp      = heightDp
    )
}