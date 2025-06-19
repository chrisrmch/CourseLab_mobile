package org.courselab.app.ui.screens.onboarding.viewModel

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.courselab.app.data.UserPreferencesDataStore
import org.courselab.app.data.repository.UserRepository
import org.courselab.app.models.MunicipioSearchResult
import org.courselab.app.ui.screens.onboarding.dto.OnboardingUiState
import org.courselab.app.ui.screens.onboarding.dto.UserProfileState
import org.courselab.app.viewmodel.BaseViewModel

class UserProfileViewModel(
    private val userRepository: UserRepository,
    private val prefs: UserPreferencesDataStore
) : BaseViewModel() {

    private val _userState = MutableStateFlow(UserProfileState())
    val userState: StateFlow<UserProfileState> = _userState.asStateFlow()

    private val _previewBitmap = MutableStateFlow<ImageBitmap?>(null)
    val previewBitmap: StateFlow<ImageBitmap?> = _previewBitmap.asStateFlow()

    private val _photoBytes = MutableStateFlow<ByteArray?>(null)
    val photoBytes: StateFlow<ByteArray?> = _photoBytes.asStateFlow()

    private val _uploadState = MutableStateFlow<OnboardingUiState>(OnboardingUiState.Idle)
    val uploadState: StateFlow<OnboardingUiState> = _uploadState.asStateFlow()

    fun cachePreview(bitmap: ImageBitmap?) {
        _previewBitmap.value = bitmap
    }

    fun cachePhotoBytes(bytes: ByteArray?) {
        _photoBytes.value = bytes
    }

    fun updateSex(sex: Sex?) {
        _userState.update { it.copy(sex = sex) }
    }

    fun updateName(nombre: String) {
        _userState.update { it.copy(nombre = nombre) }
    }

    fun updateBirthDate(fecha: kotlinx.datetime.LocalDate?) {
        _userState.update { it.copy(fechaNacimiento = fecha) }
    }

    fun setUbicacion(ubicacion: String) {
        _userState.update {
            it.copy(
                municipio = ubicacion,
            )
        }
    }

    fun setMunicipio(municipio: MunicipioSearchResult) {
        _userState.update {
            it.copy(
                municipio = "${municipio.municipio} / ${municipio.provincia}",
                municipioID = municipio.municipio_ID,
                provinciaID = municipio.provincia_ID,
                ccaaID = municipio.comunidad_autonoma_ID
            )
        }
    }

    fun setMunicipioID(municipioID: Long) {
        _userState.update { it.copy(municipioID = municipioID) }
    }

    fun setProvinciaID(provinciaID: Long) {
        _userState.update { it.copy(provinciaID = provinciaID) }
    }

    fun setCcAaID(ccaaID: Long) {
        _userState.update { it.copy(ccaaID = ccaaID) }
    }

    fun onFinishOnboarding() = scope.launch {
        val state = _userState.value
        _uploadState.value = OnboardingUiState.Loading
        try {
            val resp = userRepository.subirPerfil(
                nombre = state.nombre,
                fechaNacimiento = state.fechaNacimiento,
                fotoBytes = _photoBytes.value,
                municipioId = state.municipioID,
                provinciaId = state.provinciaID,
                ccaaId = state.ccaaID,
                peso = state.pesoKg!!,
                sex = state.sex,
                objetvo = state.objetivo
            )
            _uploadState.value = OnboardingUiState.Success(resp)
        } catch (e: Exception) {
            _uploadState.value = OnboardingUiState.Error(e.message ?: "Upload error")
        }
    }

    fun updateWeight(weight: String) {
        _userState.update { current ->
            val number = weight.toFloatOrNull()
            current.copy(
                pesoText = weight,
                pesoKg = number ?: 0f
            )
        }
    }
}
