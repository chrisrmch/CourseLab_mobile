package org.courselab.app


import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.UIDevice
import platform.UIKit.UIScreen

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