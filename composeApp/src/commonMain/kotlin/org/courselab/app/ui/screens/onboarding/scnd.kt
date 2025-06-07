package org.courselab.app.ui.screens.onboarding


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.courselab.app.ui.screens.sign_in.composables.BuildEmailTextField
import org.courselab.app.ui.screens.sign_in.composables.GradientScaffold
import org.courselab.app.ui.screens.sign_in.composables.OutlinedWelcomeButtons
import org.courselab.app.ui.screens.sign_in.composables.ThemeToggle
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject


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
    onBack: () -> Unit,
    onFinish: () -> Unit,
) {
    // 1) Estados locales
    val userViewModel = koinInject<UserViewModel>().userState
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

                    PhotoPicker(
                        currentPhotoUri = fotoPerfilUri,
                        onPickPhoto = {
                        }
                    )

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

                    // 4. Ubicación (texto)
                    BuildEmailTextField(
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
        onBack = {},
        onFinish = {}
    )
}

@Composable
fun addEmail() {
    Spacer(modifier = Modifier.height(12.dp))
    var enlaceWeb by remember { mutableStateOf("") }

    // 3. Enlace web (URL)
    BuildEmailTextField(
        fields = listOf({ enlaceWeb }),
        label = "Enlace Web",
        index = 0,
        onTextEditing = { enlaceWeb = it }
    )
}