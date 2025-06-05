@file:OptIn(ExperimentalMaterial3Api::class)

package org.courselab.app.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormatBuilder
import kotlinx.datetime.format.char
import org.courselab.app.data.UserPreferencesDataStore
import org.courselab.app.ui.screens.sign_in.composables.FormScaffold
import org.courselab.app.ui.screens.sign_in.composables.GradientScaffold
import org.courselab.app.ui.screens.sign_in.composables.OutlinedWelcomeButtons
import org.courselab.app.ui.screens.sign_in.composables.ThemeToggle
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

/**
 * OnboardingStep1: Pedimos datos básicos:
 * - nombre, apellidos (textos),
 * - fechaNacimiento (con DatePicker),
 * - género (Radio “Hombre” / “Mujer”).
 */
@Composable
fun OnboardingStep1(
    logo: Painter?,
    onNext: @Composable (
        nombre: String,
        apellidos: String,
        fechaNacimiento: LocalDate,
        genero: String
    ) -> Unit,
    onBackToLogin: () -> Unit,
) {
    // 1) Estados locales
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf<LocalDate?>( LocalDate(2004, 2, 12) ) }
    var genero by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }


    val isFormValid = nombre.isNotBlank() &&
            apellidos.isNotBlank() &&
            fechaNacimiento != null &&
            genero != null

    /**
    *El dateFormatter acepta diferentes formatos para representar la fecha.
    *Algunos ejemplos de strings que aceptaría, además del formato dd/MM/yyyy:
    *- "yyyy-MM-dd" -> dateFormatter = LocalDate.Format { year(); char('-'); monthNumber(); char('-'); dayOfMonth() }
    *- "MM/dd/yy" -> dateFormatter = LocalDate.Format { monthNumber(); char('/'); dayOfMonth(); char('/'); yearTwoDigits() }
    *- "dd.MM.yyyy" -> dateFormatter = LocalDate.Format { dayOfMonth(); char('.'); monthNumber(); char('.'); year() }
    *- "Month d, yyyy" -> dateFormatter = LocalDate.Format { monthName(MonthNames.ENGLISH_FULL); char(' '); dayOfMonth(); char(','); char(' '); year() }
    *
    *Para más detalles sobre las opciones de formato, puedes consultar la documentación de
    *kotlinx-datetime y DateTimeFormatBuilder.
    *
    *El formato actual es día/mes/año (ej: 12/02/2004)
    */
    val dateFormatter = LocalDate.Format {
        dayOfMonth(); char('/'); monthNumber(); char('/'); year()
    }

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

            logo?.let {
                Image(
                    painter = it,
                    contentDescription = "Logo CourseLab",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

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
                    Text(
                        text = "¡Bienvenido!",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Empecemos con tus datos personales:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // 1. Nombre, Apellidos (BuildTextField reusado dentro de FormScaffold)
                    FormScaffold(
                        fields = listOf(
                            "Nombre" to { nombre = it },
                            "Apellidos" to { apellidos = it }
                        ),
                        fieldValues = listOf(
                            { nombre },
                            { apellidos }
                        ),
                        onDoneAction = {
                            // Nada específico: dejamos pasar al siguiente solo con el botón
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 2. Fecha de nacimiento (TextField read-only + DatePickerDialog)
                    OutlinedTextField(
                        value = fechaNacimiento?.format(dateFormatter) ?: "",
                        onValueChange = { /* no editable manual */ },
                        readOnly = true,
                        label = { Text("Fecha de nacimiento") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                                contentDescription = "Seleccionar fecha"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker = true },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            focusedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 3. Género (RadioButton: Hombre / Mujer)
                    GenderSelector(
                        selected = genero,
                        onSelected = { genero = it }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- Botones de acción ---
                    OutlinedWelcomeButtons.Primary(
                        text = "Siguiente",
                        onClick = {
//                            fechaNacimiento?.let {
//                                genero?.let { it1 ->
//                                    onNext(nombre.trim(), apellidos.trim(),
//                                        it, it1
//                                    )
//                                }
//                            }
                        },
                        enabled = isFormValid,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedWelcomeButtons.Secondary(
                        text = "Ya tengo cuenta",
                        onClick = onBackToLogin,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))


            // --- DatePickerDialog de Material 3 ---
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Aceptar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancelar")
                        }
                    }
                ) {

                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- Toggle de tema (Light/Dark) ---
            ThemeToggle(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
        }
    }

    @Preview
    @Composable
    fun Preview_OnboardingStep1() {
        OnboardingStep1(
            logo = null,
            onNext = { _, _, _, _ -> },
            onBackToLogin = {}
        )
    }
}