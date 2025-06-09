package org.courselab.app.ui.screens.onboarding


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.layout.Measured
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.compose_multiplatform
import courselab.composeapp.generated.resources.logo
import courselab.composeapp.generated.resources.onboarding_button_back
import courselab.composeapp.generated.resources.onboarding_button_finish
import courselab.composeapp.generated.resources.onboarding_complete_final_data
import courselab.composeapp.generated.resources.onboarding_create_your_profile
import courselab.composeapp.generated.resources.profile_biography_label
import courselab.composeapp.generated.resources.profile_location_label
import org.courselab.app.HomeScreen
import org.courselab.app.LocalNavController
import org.courselab.app.ui.screens.sign_in.composables.BuildEmailTextField
import org.courselab.app.ui.screens.sign_in.composables.GradientScaffold
import org.courselab.app.ui.screens.sign_in.composables.OutlinedWelcomeButtons
import org.courselab.app.ui.screens.sign_in.composables.ThemeToggle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.ext.clearQuotes


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
    logo: Painter,
) {
    val navController = LocalNavController.current
    // 1) Estados locales
    val userViewModel = koinInject<UserViewModel>().userState
    var fotoPerfilUri by remember { mutableStateOf<String?>(null) }
    var biografia by remember { mutableStateOf("") }
    var enlaceWeb by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var intereses by remember { mutableStateOf(listOf<String>()) }

    val isFormValid = ubicacion.isNotBlank()



    var query by remember { mutableStateOf("") }
    var showSearchbar by remember { mutableStateOf(false) }

    GradientScaffold { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            HeaderOnBoardingPages(logo)
            Spacer(modifier = Modifier.height(16.dp))

            // --- Tarjeta de fondo para el formulario ---
            Surface(
                tonalElevation = 4.dp,
                shape = RoundedCornerShape(25.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // 1. Foto de perfil (PhotoPicker)

                    PhotoPicker(
                        currentPhotoUri = fotoPerfilUri, onPickPhoto = {})

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2. Biografía (campo multilínea)
                    MultilineTextField(
                        value = biografia,
                        onValueChange = { biografia = it },
                        label = stringResource(Res.string.profile_biography_label),
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )


                    Spacer(modifier = Modifier.height(12.dp))

                    // 4. Ubicación (texto)
                    SearchBar(
                        query = query,
                        onQueryChange =  {
                            query = it
                        },
                        onSearch =  {
                            query.clearQuotes()
                            query = "IME ACTION SEARHC HAS BEEN"
                        },
//                        ACTIVE TRUE -> MOSTRAR RESULTADOS, FALSE -> NO
                        active = showSearchbar,
                        onActiveChange = {
                            if( it ) {
                                showSearchbar = it
                                println("Se muestra el resultado")
                            }
                        },
                        modifier = Modifier,
                        placeholder = { Text("PLACEHOLDER") },
//                        fields = listOf({ ubicacion }),
//                        label = stringResource(Res.string.profile_location_label),
//                        index = 0,
//                        onTextEditing = { ubicacion = it }
                    ){
                        Text(text = "sdfasdfasdfasfdafda")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedWelcomeButtons.Secondary(
                            text = stringResource(Res.string.onboarding_button_back),
                            onClick = { navController?.popBackStack() },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        OutlinedWelcomeButtons.Primary(
                            text = stringResource(Res.string.onboarding_button_finish),
                            onClick = { navController?.navigate(HomeScreen) },
                            enabled = isFormValid,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- Toggle de tema (Light/Dark) ---
            ThemeToggle(
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
fun HeaderOnBoardingPages(logo: Painter) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            logo,
            contentDescription = stringResource(Res.string.logo),
            modifier = Modifier.run { size(90.dp).clip(RoundedCornerShape(15.dp)) }.weight(1f)
                .offset(x = (-20).dp)
        )
        Column(
            modifier = Modifier.weight(2f)
                .offset(y = (-12).dp, x = (-40).dp)
                .padding(horizontal = 10.dp),
        ) {
            Text(
                text = stringResource(Res.string.onboarding_create_your_profile),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(Res.string.onboarding_complete_final_data),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

class ColumnAlignmentLeft() : RowScope {
    override fun Modifier.align(alignment: Alignment.Vertical): Modifier {
        TODO("Not yet implemented")
    }

    override fun Modifier.alignBy(alignmentLine: HorizontalAlignmentLine): Modifier {
        TODO("Not yet implemented")
    }

    override fun Modifier.alignBy(alignmentLineBlock: (Measured) -> Int): Modifier {
        TODO("Not yet implemented")
    }

    override fun Modifier.alignByBaseline(): Modifier {
        TODO("Not yet implemented")
    }

    override fun Modifier.weight(weight: Float, fill: Boolean): Modifier {
        TODO("Not yet implemented")
    }

}

@Preview
@Composable
fun Preview_OnboardingStep2() {
    OnboardingStep2(logo = painterResource(Res.drawable.compose_multiplatform))
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
        onTextEditing = { enlaceWeb = it })
}