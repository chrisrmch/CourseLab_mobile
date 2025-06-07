package org.courselab.app
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform


data class ScreenDetails(
    val density: Float, // Densidad de píxeles de la pantalla
    val densityDpi: Int, // Densidad de píxeles en puntos por pulgada (DPI)
    val scaledDensity: Float,// Factor de escala para fuentes
    val widthDp: Float, // Ancho de la pantalla en píxeles independientes de la densidad (dp)
    val heightDp: Float // Alto de la pantalla en píxeles independientes de la densidad (dp)
)


expect fun screenDetails(): ScreenDetails

