﻿@file:OptIn(ExperimentalMaterial3Api::class)

package org.courselab.app.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.DateRange
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.kizitonwose.calendar.core.now
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.courselab.app.data.UserPreferencesDataStore
import org.courselab.app.ui.screens.sign_in.composables.FormField
import org.courselab.app.ui.screens.sign_in.composables.FormScaffold
import org.courselab.app.ui.screens.sign_in.composables.GradientScaffold
import org.courselab.app.ui.screens.sign_in.composables.OutlinedWelcomeButtons
import org.courselab.app.ui.screens.sign_in.composables.ThemeToggle
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject


@Composable
expect fun getDialogProperties(title: String = ""): DialogProperties

@Preview
@Composable
fun RequestDetailsCard(
    modifier: Modifier = Modifier,
    body: @Composable () -> Unit,
) {

    val datastore = koinInject<UserPreferencesDataStore>()
    val themePreferences = datastore.themePreference.collectAsState(initial = "System")
    println("theme preferemce: ${themePreferences.value}")
    Surface(
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(25.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(25.dp),
                ambientColor = MaterialTheme.colorScheme.onSecondaryContainer,
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            ).offset(y = (-4).dp, x = (-4).dp)
            .then(modifier)
    ) {
        body()
    }
}

@Composable
@Preview
fun Previews() {
    Box(
        modifier = Modifier.background(color = Color(255, 157, 157, 202)).padding(20.dp)
    ) {
        RequestDetailsCard() {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    "PREVIEW",
                    modifier = Modifier.align {
                            current: IntSize,
                            parent: IntSize,
                            layoutDirection: LayoutDirection,
                        ->
                        val x = (parent.width - current.width) / 2
                        val y = (parent.height - current.height) / 2
                        IntOffset(x, y)
                    },
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    letterSpacing = TextUnit(value = 0.5f, type = TextUnitType.Em)
                )
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInformationStep(
    logo: Painter?,
    onNext: () -> Unit,
) {
    val userViewModel = koinInject<UserViewModel>()
    val userSate by userViewModel.userState.collectAsState()

    val nombre = userSate.name
    val apellidos = userSate.surname
    val fechaNacimiento = userSate.dateOfBirth
    val userSex = userSate.sex
    var genero by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    val maxDateMillis = remember {
        Clock.System.now().toEpochMilliseconds()
    }

//    val r = remember {
//        DatePickerState(
//            locale = CalendarLocale("es", "ES"),
//            initialSelectedDateMillis = null,
//            yearRange = 1950..LocalDate.now().year,
//            initialDisplayMode = DisplayMode.Picker,
//            selectableDates = /* tu SelectableDates */
//        )
//    }

    val datePickerState = rememberDatePickerState(

        yearRange = IntRange(1950, LocalDate.now().year),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= maxDateMillis
            }
        },
    )

    val datePickerFormatter = remember {
        DatePickerDefaults.dateFormatter(
            selectedDateDescriptionSkeleton = "dd MMMM yyyy", // Changed to include spaces and full month name
            yearSelectionSkeleton = "yyyy",
            selectedDateSkeleton = "MMMM d", // Changed to include day of the month
        )
    }

    val stringDateFormatter = LocalDate.Format {
        dayOfMonth()
        char('/')
        monthNumber()
        char('/')
        year()
    }

    val isFormValid = remember(nombre, apellidos, fechaNacimiento, genero) {
        nombre.isNotBlank()
                && apellidos.isNotBlank()
                && fechaNacimiento != null
                && userSex != null
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

            RequestDetailsCard(modifier = Modifier) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "¡Bienvenido ${nombre}!",
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

                    FormScaffold(
                        fields = listOf(
                            FormField("Nombre", {
                                userViewModel.updateUserName(it)
                            }),
                            FormField("Apellidos", { userViewModel.updateUserSurname(it) })
                        ),
                        fieldValues = listOf(
                            { nombre },
                            { apellidos }
                        ),
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FechaConOverlay(
                        fecha = fechaNacimiento?.format(stringDateFormatter) ?: "",
                        onClickAbrirPicker = { showDatePicker = true },
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (userSex == null) {
                        GenderSelector(
                            selected = genero,
                            onSelected = {
                                genero = it
                                if (genero?.lowercase() == "hombre") {
                                    userViewModel.updateUserSex(Sex.HOMBRE)
                                } else {
                                    userViewModel.updateUserSex(Sex.MUJER)
                                }
                            }
                        )
                    } else {
                        GenderSelector(
                            selected = userSex.name,
                            onSelected = {
                                genero = it
                                if (genero?.lowercase() == "hombre") {
                                    userViewModel.updateUserSex(Sex.HOMBRE)
                                } else {
                                    userViewModel.updateUserSex(Sex.MUJER)
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))


                    OutlinedWelcomeButtons.Primary(
                        text = "Siguiente", //TODO("i18n Must do Internationalization")
                        onClick = { onNext() },
                        enabled = isFormValid,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (showDatePicker) {
                DatePickerDialog(
                    properties = getDialogProperties(title = "Selecciona tu fecha de nacimiento"),
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let { dateOfBirthMillis ->
                                    val instant = Instant.fromEpochMilliseconds(dateOfBirthMillis)
                                    val selectedLocalDate = instant
                                        .toLocalDateTime(TimeZone.currentSystemDefault())
                                        .date
                                    userViewModel.updateUserDateOfBirth(selectedLocalDate)
                                }
                                showDatePicker = false
                            },
                            enabled = datePickerState.selectedDateMillis != null
                        ) {
                            Text("Aceptar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancelar")
                        }
                    }
                ) {

                    DatePicker(
                        state = datePickerState,
                        // … tus otros parámetros …
                        headline = {
                            Column {
                                // 1) el “headline” por defecto, para conservar la fecha dinámica y el toggle de modo
                                DatePickerDefaults.DatePickerHeadline(
                                    selectedDateMillis = datePickerState.selectedDateMillis,
                                    displayMode = datePickerState.displayMode,
                                    dateFormatter = datePickerFormatter,
                                    modifier = Modifier.padding(PaddingValues(
                                        start = 24.dp,
                                        end = 12.dp,
                                        bottom = 12.dp
                                    )),
                                )
                                // 2) tu texto estático, justo debajo o encima
                                Text(
                                    text = "Selecciona tu fecha de nacimiento",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(start = 24.dp, end = 12.dp, bottom = 12.dp)
                                )
                            }
                        }
                    )

                    DatePicker(
                        state = datePickerState,
                        dateFormatter = datePickerFormatter,
                        title = {
                            Text(
                                text = "Fecha de nacimiento",
                                modifier = Modifier.padding(
                                    PaddingValues(
                                        start = 24.dp,
                                        end = 12.dp,
                                        top = 16.dp
                                    )
                                ),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
//                        headline = {
//                            DatePickerDefaults.DatePickerHeadline(
//                                selectedDateMillis = null,
//                                displayMode = datePickerState.displayMode,
//                                dateFormatter = datePickerFormatter,
//                                modifier = Modifier.padding(
//                                    paddingValues = PaddingValues(
//                                        start = 24.dp,
//                                        end = 12.dp,
//                                        bottom = 12.dp
//                                    )
//                                )
//                            )
//                            Text(
//                                "Selecciona tu fecha de nacimiento",
//                                style = MaterialTheme.typography.labelLarge,
//                                color = MaterialTheme.colorScheme.onSurfaceVariant,
//                                modifier = Modifier.padding(
//                                    paddingValues = PaddingValues(
//                                        start = 24.dp,
//                                        end = 12.dp,
//                                        bottom = 12.dp
//                                    )
//                                )
//                            )
//                        },
                        colors = DatePickerDefaults.colors(
                            selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                            selectedDayContentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                    )
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
}


@Composable
fun Preview_OnboardingStep1() {
    UserInformationStep(
        logo = null,
        onNext = {},
    )
}

@Composable
fun FechaConOverlay(
    fecha: String,
    onClickAbrirPicker: () -> Unit,
) {


    Box(modifier = Modifier.fillMaxWidth().clipToBounds().offset(y = (-2).dp)) {
        OutlinedTextField(
            value = fecha,
            onValueChange = {},
            readOnly = true,
            label = { Text("Fecha de nacimiento") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Sharp.DateRange,
                    contentDescription = "Seleccionar fecha"
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
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
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { onClickAbrirPicker() }
        )
    }

}
