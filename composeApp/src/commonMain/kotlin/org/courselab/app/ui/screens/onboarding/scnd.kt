package org.courselab.app.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.courselab.app.ui.screens.sign_in.composables.BuildTextField
import org.courselab.app.ui.screens.sign_in.composables.GradientScaffold
import org.courselab.app.ui.screens.sign_in.composables.OutlinedWelcomeButtons
import org.courselab.app.ui.screens.sign_in.composables.ThemeToggle
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * OnboardingStep2: Pedimos datos de perfil adicionales:
 * - fotoPerfil (PhotoPicker)
 * - biografia (Multilínea)
 * - enlaceWeb (URL)
 * - ubicacion (texto)
 * - intereses (lista dinámica de chips)
 */
@Composable
fun OnboardingStep2(
    initialNombre: String,
    initialApellidos: String,
    initialFechaNacimiento: LocalDate,
    initialGenero: String,
    onBack: () -> Unit,
    onFinish: (
        fotoPerfilUri: String?,
        biografia: String,
        enlaceWeb: String,
        ubicacion: String,
        intereses: List<String>
    ) -> Unit
) {
    // 1) Estados locales
    var fotoPerfilUri by remember { mutableStateOf<String?>(null) }
    var biografia by remember { mutableStateOf("") }
    var enlaceWeb by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var intereses by remember { mutableStateOf(listOf<String>()) }

    // Validación: biografía obligatoria, ubicación obligatoria (o como prefieras)
    val isFormValid = ubicacion.isNotBlank()

    GradientScaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // --- Título contextual ---
            Text(
                text = "¡Crea tu perfil!",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Completa los datos finales:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Tarjeta de fondo para el formulario ---
            Surface(
                tonalElevation = 4.dp,
                shape = RoundedCornerShape(25.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // 1. Foto de perfil (PhotoPicker)

//                    PhotoPicker(
//                        currentPhotoUri = fotoPerfilUri,
//                        onPickPhoto = {
//                            // Llamar a la capa nativa para abrir selector de imágenes
//                            // La implementación concreta se hace en Android/iOS/desktop
//                            // Aquí simplemente disparamos un callback
//                            onPickPhotoImpl = { nuevaUri ->
//                                fotoPerfilUri = nuevaUri
//                            }
//                        }
//                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2. Biografía (campo multilínea)
                    MultilineTextField(
                        value = biografia,
                        onValueChange = { biografia = it },
                        label = "Biografía",
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 3. Enlace web (URL)
                    BuildTextField(
                        fields = listOf({ enlaceWeb }),
                        label = "Enlace Web",
                        index = 0,
                        onTextEditing = { enlaceWeb = it }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 4. Ubicación (texto)
                    BuildTextField(
                        fields = listOf({ ubicacion }),
                        label = "Ubicación",
                        index = 0,
                        onTextEditing = { ubicacion = it }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 5. Intereses (chips dinámicos)
                    InterestsInput(
                        interests = intereses,
                        onAddInterest = { newInterest ->
                            if (newInterest.isNotBlank() && !intereses.contains(newInterest.trim())) {
                                intereses = intereses + newInterest.trim()
                            }
                        },
                        onRemoveInterest = { toRemove ->
                            intereses = intereses - toRemove
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- Botones de acción ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedWelcomeButtons.Secondary(
                            text = "Atrás",
                            onClick = onBack,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        OutlinedWelcomeButtons.Primary(
                            text = "Finalizar",
                            onClick = {
                                onFinish(
                                    fotoPerfilUri,
                                    biografia.trim(),
                                    enlaceWeb.trim(),
                                    ubicacion.trim(),
                                    intereses
                                )
                            },
                            enabled = isFormValid,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- Toggle de tema (Light/Dark) ---
            ThemeToggle(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Preview
@Composable
fun Preview_OnboardingStep2() {
    OnboardingStep2(
        initialNombre = "Juan",
        initialApellidos = "Pérez",
        initialFechaNacimiento = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
        initialGenero = "Hombre",
        onBack = {},
        onFinish = { _, _, _, _, _ -> }
    )
}
