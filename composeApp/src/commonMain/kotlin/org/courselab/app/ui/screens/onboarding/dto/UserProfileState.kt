package org.courselab.app.ui.screens.onboarding.dto

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.datetime.LocalDate
import org.courselab.app.ui.screens.onboarding.viewModel.Sex

data class UserProfileState(
    val nombre: String = "",
    val fechaNacimiento: LocalDate? = null,
    val sex : Sex? = null,
    val photoBytes: ImageBitmap? = null,
    val municipio: String = "",
    val municipioID: Long? = null,
    val provinciaID: Long? = null,
    val ccaaID: Long? = null,
    val pesoText: String = "",
    val pesoKg: Float? = null,
    val objetivo: Int = 0,
    val foto : String = ""
)
