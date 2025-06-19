package org.courselab.app.ui.screens.onboarding.dto
import org.courselab.app.models.PerfilUsuarioResponseDTO


sealed class OnboardingUiState {
    object Idle : OnboardingUiState()
    object Loading : OnboardingUiState()
    data class Success(val response: PerfilUsuarioResponseDTO) : OnboardingUiState()
    data class Error(val message: String) : OnboardingUiState()
}