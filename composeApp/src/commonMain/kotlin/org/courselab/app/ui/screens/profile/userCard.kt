package org.courselab.app.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.change_lang
import courselab.composeapp.generated.resources.date
import courselab.composeapp.generated.resources.edit_profile
import courselab.composeapp.generated.resources.email
import courselab.composeapp.generated.resources.profile
import courselab.composeapp.generated.resources.profile_location_label
import courselab.composeapp.generated.resources.sex
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.courselab.app.LocalUrlLauncher
import org.courselab.app.UrlLauncher
import org.courselab.app.data.repository.UserRepository
import org.courselab.app.models.PerfilUsuarioResponseDTO
import org.courselab.app.ui.screens.sign_in.composables.GradientScaffold
import org.courselab.app.ui.screens.sign_in.composables.ThemeToggle
import org.courselab.app.viewmodel.BaseViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Serializable
data class UsuarioResponseDTO(
    val idUsuario: Long? = null,
    val perfil: PerfilUsuarioResponseDTO? = null,
    val nombre: String? = null,
    val email: String? = null,
    val fechaNacimiento: String? = null,
    val genero: String? = null
)

@Serializable
data class UsuarioPageState(
    val idUsuario: Long? = 0,
    val perfil: PerfilUsuarioResponseDTO? = null,
    val nombre: String = "",
    val email: String = "",
    val fechaNacimiento: String? = "",
    val genero: String = ""
)


class UserPageStateViewModel(
    val userRepository: UserRepository
) : BaseViewModel() {
    val _userState = MutableStateFlow(UsuarioPageState())
    val userState = _userState

    init {
        scope.launch {
            runCatching { userRepository.getCurrentUser() }
                .onSuccess { user ->
                    _userState.update { it ->
                        it.copy(
                            idUsuario = user?.idUsuario,
                            perfil = user?.perfil,
                            nombre = user?.nombre ?: "",
                            email = user?.email ?: "",
                            fechaNacimiento = user?.fechaNacimiento,
                            genero = user?.genero ?: ""
                        )
                    }
                }
                .onFailure {
                    println("Error al obtener el usuario: $it")
                }
        }
    }
}

@Composable
fun UserProfileDetailsScreen(
    userPageStateViewModel: UserPageStateViewModel = koinInject()
) {
    val state = userPageStateViewModel.userState.value
    val profileImageUrl = state.perfil?.fotoPerfilUrl ?: "https://via.placeholder.com/300"
    val urlLauncher = LocalUrlLauncher.current


    GradientScaffold {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Box(modifier = Modifier.clip(shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .height(300.dp)
                .fillMaxWidth()
            ) {
                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(modifier = Modifier
                    .matchParentSize()
                    .clip(MaterialTheme.shapes.medium)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent),
                            startY = 0f,
                            endY = 200f
                        )
                    )
                )
                Box(modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .align(Alignment.BottomCenter)
                ) {
                    AsyncImage(
                        model = state.perfil?.fotoPerfilUrl,
                        contentDescription = stringResource(Res.string.profile),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxHeight()) {
                    Text(
                        text = state.nombre.ifEmpty { "Usuario" },
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    ProfileDetailRow(icon = Icons.Filled.Email, label = stringResource(Res.string.email), value = state.email)
                    ProfileDetailRow(icon = Icons.Filled.DateRange, label = stringResource(Res.string.date), value = state.fechaNacimiento.orEmpty())
                    ProfileDetailRow(icon = Icons.Filled.Person, label = stringResource(Res.string.sex), value = state.genero)

                    state.perfil?.localizacion?.let {
                        ProfileDetailRow(icon = Icons.Filled.LocationOn, label = stringResource(Res.string.profile_location_label), value = it.nombre)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        ChangeLanguageButton(urlLauncher)
                        ThemeToggle()
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun ProfileDetailRow(icon: ImageVector, label: String, value: String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ChangeLanguageButton(urlLauncher: UrlLauncher?) {
    Button(
        modifier = Modifier,
        onClick = {
            urlLauncher?.openAppSettings()
        }
    ) {
        Text(stringResource(Res.string.change_lang))
    }
}