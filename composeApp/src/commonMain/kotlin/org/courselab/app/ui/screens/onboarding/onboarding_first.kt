@file:OptIn(ExperimentalMaterial3Api::class)

package org.courselab.app.ui.screens.onboarding

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode.Companion.Picker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
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
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.kizitonwose.calendar.core.now
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.accept
import courselab.composeapp.generated.resources.cancel
import courselab.composeapp.generated.resources.date_picker_title
import courselab.composeapp.generated.resources.female
import courselab.composeapp.generated.resources.male
import courselab.composeapp.generated.resources.name
import courselab.composeapp.generated.resources.next
import courselab.composeapp.generated.resources.permission_profile_picture_message
import courselab.composeapp.generated.resources.permission_required
import courselab.composeapp.generated.resources.select_dob
import courselab.composeapp.generated.resources.settings
import courselab.composeapp.generated.resources.weight
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.courselab.app.CalendarLocaleWithLanguage
import org.courselab.app.PermissionType
import org.courselab.app.rememberCameraManager
import org.courselab.app.rememberGalleryManager
import org.courselab.app.ui.screens.AlertMessageDialog
import org.courselab.app.ui.screens.ImageSourceOptionDialog
import org.courselab.app.ui.screens.onboarding.composables.FechaConOverlay
import org.courselab.app.ui.screens.onboarding.composables.GenderCard
import org.courselab.app.ui.screens.onboarding.composables.NumberField
import org.courselab.app.ui.screens.onboarding.composables.PhotoPicker
import org.courselab.app.ui.screens.onboarding.composables.RequestDetailsCard
import org.courselab.app.ui.screens.onboarding.viewModel.Sex
import org.courselab.app.ui.screens.onboarding.viewModel.UserProfileViewModel
import org.courselab.app.ui.screens.sign_in.composables.FormField
import org.courselab.app.ui.screens.sign_in.composables.FormScaffold
import org.courselab.app.ui.screens.sign_in.composables.GradientScaffold
import org.courselab.app.ui.screens.sign_in.composables.OutlinedWelcomeButtons
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject


@Composable
expect fun getDialogProperties(title: String = ""): DialogProperties


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileDetailsScreenFirst(
    logo: Painter,
    onNext: () -> Unit,
) {
    val userProfileViewModel = koinInject<UserProfileViewModel>()
    val calendarLocale: CalendarLocaleWithLanguage = koinInject()
    val userState = userProfileViewModel.userState.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }
    val maxDateMillis = remember {
        Clock.System.now().toEpochMilliseconds()
    }

    val datePickerState = datePickerState(calendarLocale, maxDateMillis)

    val stringDateFormatter = dateTimeFormat()
    val isFormValid =
        remember(userState.value.nombre, userState.value.fechaNacimiento, userState.value.sex) {
            userState.value.nombre.isNotEmpty()
                    && userState.value.fechaNacimiento != null
                    && userState.value.sex != null
        }

    var showImageSourceDialog by remember { mutableStateOf(value = false) }
    var launchCamera by remember { mutableStateOf(value = false) }
    var launchGallery by remember { mutableStateOf(value = false) }
    var permissionRationalDialog by remember { mutableStateOf(value = false) }
    var launchSetting by remember { mutableStateOf(value = false) }


    val permissionsManager = fetchActualPermissionManager(
        launchCamera = { launchCamera = it },
        launchGallery = { launchGallery = it },
        permissionRationalDialog = { permissionRationalDialog = it }
    )


    val cameraManager = rememberCameraManager { shared ->
        shared?.let {
            val bmp = it.toImageBitmap()
            val bytes = it.toByteArray()
            userProfileViewModel.cachePreview(bmp)
            userProfileViewModel.cachePhotoBytes(bytes)
        }
    }

    val galleryManager = rememberGalleryManager { shared ->
        shared?.let {
            val bmp = it.toImageBitmap()
            val bytes = it.toByteArray()
            userProfileViewModel.cachePreview(bmp)
            userProfileViewModel.cachePhotoBytes(bytes)
        }
    }

    if (launchSetting) {
        permissionsManager.launchSettings()
        launchSetting = false
    }
    if (permissionRationalDialog) {
        AlertMessageDialog(
            title = stringResource(Res.string.permission_required),
            message = stringResource(Res.string.permission_profile_picture_message),
            positiveButtonText = stringResource(Res.string.settings),
            negativeButtonText = stringResource(Res.string.cancel),
            onPositiveClick = {
                permissionRationalDialog = false
                launchSetting = true

            },
            onNegativeClick = {
                permissionRationalDialog = false
            })
    }

    if (showImageSourceDialog) {
        ImageSourceOptionDialog(
            onDismissRequest = { showImageSourceDialog = false },
            onCameraRequest = {
                showImageSourceDialog = false
                launchCamera = true
            },
            onGalleryRequest = {
                showImageSourceDialog = false
                launchGallery = true
            }
        )
    }

    if (launchCamera) {
        if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
            cameraManager.launch()
        } else {
            permissionsManager.askPermission(PermissionType.CAMERA)
        }
        launchCamera = false
    }

    if (launchGallery) {
        if (permissionsManager.isPermissionGranted(PermissionType.GALLERY)) {
            galleryManager.launch()
        } else {
            permissionsManager.askPermission(PermissionType.GALLERY)
        }
        launchGallery = false
    }

    GradientScaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues).clearFocusOnTapOutside().windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            HeaderOnBoardingPages(logo)
            Spacer(modifier = Modifier.height(10.dp))

            RequestDetailsCard(modifier = Modifier) {
                Column(modifier = Modifier.padding(20.dp)) {
                    PhotoPickerWrapper(
                        photo = userProfileViewModel.previewBitmap.collectAsState().value,
                        onOpenPicker = { showImageSourceDialog = true },
                        modifier = Modifier
                    )

                    FormScaffold(
                        fields = listOf(
                            FormField(stringResource(Res.string.name), {
                                userProfileViewModel.updateName(it)
                            }),
                        ),
                        fieldValues = listOf(
                            { userState.value.nombre },
                        ),
                    )

                    FechaConOverlay(
                        fecha = userState.value.fechaNacimiento?.format(stringDateFormatter) ?: "",
                        onClickAbrirPicker = { showDatePicker = true },
                        modifier = Modifier
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    NumberField(
                        value = userState.value.pesoText,
                        onValueChange = { it -> userProfileViewModel.updateWeight(it) },
                        label = stringResource(Res.string.weight),
                        modifier = Modifier
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            GenderCard(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .fillMaxWidth(),
                                selectedGender = if (userState.value.sex == Sex.HOMBRE) Sex.HOMBRE else null,
                                cardGender = Sex.HOMBRE,
                                onGenderSelected = { userProfileViewModel.updateSex(Sex.HOMBRE) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                text = stringResource(Res.string.male),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.weight(0.1f))
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            GenderCard(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .fillMaxWidth(),
                                selectedGender = if (userState.value.sex == Sex.MUJER) Sex.MUJER else null,
                                cardGender = Sex.MUJER,
                                onGenderSelected = { userProfileViewModel.updateSex(Sex.MUJER) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                text = stringResource(Res.string.female),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))


                    OutlinedWelcomeButtons.Primary(
                        text = stringResource(Res.string.next),
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
                                    userProfileViewModel.updateBirthDate(selectedLocalDate)
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

        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun datePickerState(
    calendarLocale: CalendarLocaleWithLanguage,
    maxDateMillis: Long
) = remember {
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

private fun dateTimeFormat() = LocalDate.Format {
    dayOfMonth()
    char('/')
    monthNumber()
    char('/')
    year()
}

fun Modifier.clearFocusOnTapOutside(): Modifier = composed {
    val focusManager = LocalFocusManager.current
    pointerInput(Unit) {
        detectTapGestures(onTap = { focusManager.clearFocus() })
    }
}

@Composable
private fun PhotoPickerWrapper(
    photo: ImageBitmap?,
    onOpenPicker: () -> Unit,
    modifier: Modifier
) {
    PhotoPicker(
        modifier = modifier.clipToBounds(),
        currentPhoto = photo,
        onClick = onOpenPicker
    )
}
