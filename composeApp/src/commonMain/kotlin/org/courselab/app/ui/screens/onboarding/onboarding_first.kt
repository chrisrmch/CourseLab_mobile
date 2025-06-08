@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode.Companion.Picker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.kizitonwose.calendar.core.now
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.accept
import courselab.composeapp.generated.resources.app_name
import courselab.composeapp.generated.resources.cancel
import courselab.composeapp.generated.resources.date_picker_title
import courselab.composeapp.generated.resources.logo
import courselab.composeapp.generated.resources.name
import courselab.composeapp.generated.resources.next
import courselab.composeapp.generated.resources.select_dob
import courselab.composeapp.generated.resources.surname
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.courselab.app.CalendarLocaleWithLanguage
import org.courselab.app.data.UserPreferencesDataStore
import org.courselab.app.ui.screens.sign_in.composables.FormField
import org.courselab.app.ui.screens.sign_in.composables.FormScaffold
import org.courselab.app.ui.screens.sign_in.composables.GradientScaffold
import org.courselab.app.ui.screens.sign_in.composables.OutlinedWelcomeButtons
import org.courselab.app.ui.screens.sign_in.composables.ThemeToggle
import org.jetbrains.compose.resources.stringResource
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

    val calendarLocale: CalendarLocaleWithLanguage = koinInject()

    val datePickerState = remember {
        DatePickerState(
            locale = calendarLocale.getPlatformCalendarLocale(),
            yearRange = 1950..LocalDate.now().year,
            initialDisplayMode = Picker,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= maxDateMillis
                }
            },
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
                    contentDescription = "${stringResource(Res.string.logo)} ${stringResource(Res.string.app_name)}",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            RequestDetailsCard(modifier = Modifier) {
                //TODO("ADAPT TEXT TO GRAMMAR RULES") IF FR -> {} : {}
                Column(modifier = Modifier.padding(20.dp)) {
                    Spacer(modifier = Modifier.height(16.dp))

                    FormScaffold(
                        fields = listOf(
                            FormField(stringResource(Res.string.name), {
                                userViewModel.updateUserName(it)
                            }),
                            FormField(
                                stringResource(Res.string.surname),
                                { userViewModel.updateUserSurname(it) })
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
                                println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!---------------- $it")
                                genero = it
                                if (genero?.lowercase() == Sex.HOMBRE.name.lowercase()) {
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
                                if (genero?.lowercase() == Sex.HOMBRE.name.lowercase()) {
                                    userViewModel.updateUserSex(Sex.HOMBRE)
                                } else {
                                    userViewModel.updateUserSex(Sex.MUJER)
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))


                    OutlinedWelcomeButtons.Primary(
                        text = stringResource(Res.string.next), //TODO("i18n Must do Internationalization")
                        onClick = { onNext() },
                        enabled = isFormValid,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (showDatePicker) {
                DatePickerDialog(
                    properties = getDialogProperties(title = stringResource(Res.string.date_picker_title)),
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
                            Text(stringResource(Res.string.accept))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text(stringResource(Res.string.cancel))
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState,
                        title = {
                            Text(
                                text = stringResource(Res.string.select_dob),
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
                        colors = DatePickerDefaults.colors(
                            selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                            selectedDayContentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ThemeToggle(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
        }
    }
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
            label = { Text(stringResource(Res.string.date_picker_title)) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Sharp.DateRange,
                    contentDescription = stringResource(Res.string.select_dob)
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