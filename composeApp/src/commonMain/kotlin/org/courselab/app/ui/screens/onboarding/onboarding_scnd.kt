@file:OptIn(ExperimentalMaterial3Api::class)

package org.courselab.app.ui.screens.onboarding


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.compose_multiplatform
import courselab.composeapp.generated.resources.logo
import courselab.composeapp.generated.resources.onboarding_button_back
import courselab.composeapp.generated.resources.onboarding_button_finish
import courselab.composeapp.generated.resources.onboarding_complete_final_data
import courselab.composeapp.generated.resources.onboarding_create_your_profile
import courselab.composeapp.generated.resources.profile_biography_label
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import org.courselab.app.PermissionCallback
import org.courselab.app.PermissionStatus
import org.courselab.app.PermissionType
import org.courselab.app.createPermissionsManager
import org.courselab.app.org.courselab.app.HomeScreen
import org.courselab.app.org.courselab.app.LocalNavController
import org.courselab.app.org.courselab.app.models.MunicipioSearchResult
import org.courselab.app.org.courselab.app.ui.screens.AlertMessageDialog
import org.courselab.app.org.courselab.app.ui.screens.ImageSourceOptionDialog
import org.courselab.app.org.courselab.app.ui.screens.onboarding.MunicipioSearchViewModel
import org.courselab.app.rememberCameraManager
import org.courselab.app.rememberGalleryManager
import org.courselab.app.screenDetails
import org.courselab.app.ui.screens.log_in.composables.GradientScaffold
import org.courselab.app.ui.screens.log_in.composables.OutlinedWelcomeButtons
import org.courselab.app.ui.screens.log_in.composables.ThemeToggle
import org.courselab.app.viewmodel.BaseViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject


data class userProfile(
    val nombre: String = "",
    val apellidos: String = "",
    val fechaNacimiento: LocalDate? = null,
    val fotoPerfil: String = "",
    val descrpcionPersonal: String = "",
    val ubicacion: String = "",
)


class UserProfileViewModel : BaseViewModel() {
    private val _userProfileState = MutableStateFlow(userProfile())
    val userState = _userProfileState

    fun setNombre(nombre: String) {
        _userProfileState.value = _userProfileState.value.copy(nombre = nombre)
    }

    fun setApellidos(apellidos: String) {
        _userProfileState.value = _userProfileState.value.copy(apellidos = apellidos)
    }

    fun setFechaNacimiento(fecha: LocalDate?) {
        _userProfileState.value = _userProfileState.value.copy(fechaNacimiento = fecha)
    }

    fun setFotoPerfil(fotoUri: String) {
        _userProfileState.value = _userProfileState.value.copy(fotoPerfil = fotoUri)
    }

    fun setDescripcionPersonal(descripcion: String) {
        if (descripcion.length <= 80) {
            _userProfileState.value = _userProfileState.value.copy(descrpcionPersonal = descripcion)
        } else {
            // Optionally, handle the case where the description is too long, e.g., show an error or truncate.
        }
    }

    fun setUbicacion(ubicacion: String) {
        _userProfileState.value = _userProfileState.value.copy(ubicacion = ubicacion)
    }
}


@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun OnboardingStep2(
    logo: Painter,
) {
    val coroutineScope = rememberCoroutineScope()
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var imageSourceOptionDialog by remember { mutableStateOf(value = false) }
    var launchCamera by remember { mutableStateOf(value = false) }
    var launchGallery by remember { mutableStateOf(value = false) }
    var launchSetting by remember { mutableStateOf(value = false) }
    var permissionRationalDialog by remember { mutableStateOf(value = false) }
    val permissionsManager = createPermissionsManager(object : PermissionCallback {
        override fun onPermissionStatus(
            permissionType: PermissionType,
            status: PermissionStatus,
        ) {
            when (status) {
                PermissionStatus.GRANTED -> {
                    when (permissionType) {
                        PermissionType.CAMERA -> launchCamera = true
                        PermissionType.GALLERY -> launchGallery = true
                    }
                }

                else -> {
                    permissionRationalDialog = true
                }
            }
        }


    })

    val cameraManager = rememberCameraManager {
        coroutineScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                it?.toImageBitmap()
            }
            imageBitmap = bitmap
        }
    }

    val galleryManager = rememberGalleryManager {
        coroutineScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                it?.toImageBitmap()
            }
            imageBitmap = bitmap
        }
    }
    if (imageSourceOptionDialog) {
        ImageSourceOptionDialog(onDismissRequest = {
            imageSourceOptionDialog = false
        }, onGalleryRequest = {
            imageSourceOptionDialog = false
            launchGallery = true
        }, onCameraRequest = {
            imageSourceOptionDialog = false
            launchCamera = true
        })
    }
    if (launchGallery) {
        if (permissionsManager.isPermissionGranted(PermissionType.GALLERY)) {
            galleryManager.launch()
        } else {
            permissionsManager.askPermission(PermissionType.GALLERY)
        }
        launchGallery = false
    }
    if (launchCamera) {
        if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
            cameraManager.launch()
        } else {
            permissionsManager.askPermission(PermissionType.CAMERA)
        }
        launchCamera = false
    }
    if (launchSetting) {
        permissionsManager.launchSettings()
        launchSetting = false
    }
    if (permissionRationalDialog) {
        AlertMessageDialog(
            title = "Permission Required",
            message = "To set your profile picture, please grant this permission. You can manage permissions in your device settings.",
            positiveButtonText = "Settings",
            negativeButtonText = "Cancel",
            onPositiveClick = {
                permissionRationalDialog = false
                launchSetting = true

            },
            onNegativeClick = {
                permissionRationalDialog = false
            })
    }

    //--------------------------------------------------------------------------------
    val navController = LocalNavController.current
    val userProfileVM = koinInject<UserProfileViewModel>()
    val profile = userProfileVM.userState.collectAsState()
    val searchViewModel: MunicipioSearchViewModel = koinInject()
    LaunchedEffect(Unit) { searchViewModel.initialize() }
    val query by searchViewModel.inputText.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val viewState by searchViewModel.viewState.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    val ubicacion by remember { mutableStateOf(profile.value.ubicacion) }
    val isFormValid = ubicacion.isNotBlank()

    val items = when (viewState) {
        is MunicipioSearchViewModel.ViewState.MunicipioSearchResultFetch -> (viewState as MunicipioSearchViewModel.ViewState.MunicipioSearchResultFetch).municipios
        else -> emptyList()
    }

    LaunchedEffect(query.isEmpty()) {
        if (query.isEmpty()) focusRequester.requestFocus()
    }

    val itemHeight = 56.dp
    val screenHeightDp = screenDetails().heightDp.dp
    val maxTableHeight = screenHeightDp * (2f / 3f)
    val tableHeight = (itemHeight * items.size.toFloat()).coerceAtMost(maxTableHeight)

    GradientScaffold(
        modifier = Modifier.pointerInput(expanded) {
            detectTapGestures {
                if (expanded) {
                    expanded = false
                }
            }
        }) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            HeaderOnBoardingPages(logo)
            Spacer(modifier = Modifier.height(10.dp))

            RequestDetailsCard(modifier = Modifier) {
                Column(modifier = Modifier.padding(20.dp)) {
                    PhotoPickerWrapper(profile, imageSourceOptionDialog)
                    Spacer(modifier = Modifier.height(10.dp))


                    // SEARCH BAR
                    AdaptiveDockedSearchBar(
                        inputField = {
                            val displayText = profile.value.ubicacion.ifEmpty { query }
                            SearchInputField(
                                text = displayText,
                                placeholder = "Buscar municipio",
                                onValueChange = {
                                    searchViewModel.updateInput(it)
                                    if (!expanded) expanded = true
                                },
                                onClear = {
                                    searchViewModel.clearInput()
                                },
                                showClear = query.isNotBlank(),
                                enabled = profile.value.ubicacion.isBlank(),
                            )
                        },
                        colors = SearchBarDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            dividerColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier,
                        tonalElevation = 4.dp,
                        shadowElevation = 8.dp,
                        expanded = expanded
                    ) {
                        // CONTENIDO DEL DROPDOWN
                        when (viewState) {
                            MunicipioSearchViewModel.ViewState.Loading -> LoadingIndicator()
                            MunicipioSearchViewModel.ViewState.NoResults -> NoResultsMessage()
                            is MunicipioSearchViewModel.ViewState.MunicipioSearchResultFetch -> {
                                SearchResultsList(
                                    items = (viewState as MunicipioSearchViewModel.ViewState.MunicipioSearchResultFetch).municipios,
                                    onItemClick = { m ->
                                        userProfileVM.setUbicacion(m.municipio)
                                        searchViewModel.revertToInitialState()
                                        expanded = false
                                    }
                                )
                            }
                            MunicipioSearchViewModel.ViewState.Error -> ErrorMessage()
                            else -> HintMessage("Teclea para buscar tu municipio")
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    MultilineTextField(
                        value = profile.value.descrpcionPersonal,
                        onValueChange = { userProfileVM.setDescripcionPersonal(it) },
                        label = stringResource(Res.string.profile_biography_label),
                        minLines = 1,
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )

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
            ThemeToggle(
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchInputField(
    text: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    showClear: Boolean,
    enabled: Boolean
) {
    SearchBarDefaults.InputField(
        query = text,
        onQueryChange = { if (enabled) onValueChange(it) },
        onSearch = { /* opcional */ },
        expanded = true,
        onExpandedChange = { /* noop */ },
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(Icons.Default.Search, null) },
        trailingIcon = {
            if (showClear) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Default.Close, null)
                }
            }
        },
        colors = CustomInputFieldColors()
    )
}


@Composable
private fun SearchResultsList(
    items: List<MunicipioSearchResult>,
    onItemClick: (MunicipioSearchResult) -> Unit
) {
    LazyColumn {
        items(items, key = { it.municipio_ID }) { municipio ->
            ListItem(
                headlineContent = { Text(municipio.municipio) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(municipio) }
            )
            HorizontalDivider()
        }
    }
}

@Composable private fun LoadingIndicator() = Box(
    Modifier.fillMaxWidth().padding(16.dp),
    contentAlignment = Alignment.Center
) { CircularProgressIndicator() }

@Composable private fun NoResultsMessage() = Text(
    "No hay resultados",
    Modifier.fillMaxWidth().padding(16.dp),
    color = MaterialTheme.colorScheme.error
)

@Composable private fun ErrorMessage() = Text(
    "Error al buscar",
    Modifier.fillMaxWidth().padding(16.dp),
    color = MaterialTheme.colorScheme.error
)

@Composable private fun HintMessage(text: String) = Text(
    text,
    Modifier.fillMaxWidth().padding(16.dp),
    color = MaterialTheme.colorScheme.onSurfaceVariant
)
@Composable
private fun CustomInputFieldColors() =  TextFieldDefaults.colors(
    focusedTextColor = MaterialTheme.colorScheme.onSurface,
    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
    cursorColor = MaterialTheme.colorScheme.primary,
    selectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colorScheme.primary,
        backgroundColor = MaterialTheme.colorScheme.primary.copy(
            alpha = 0.4f
        )
    ),
    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
    unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    focusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,

)

@Composable
private fun PhotoPickerWrapper(
    userProfileState: State<userProfile>, imageSourceOptionDialog: Boolean
) {
    var imageSourceOptionDialog1 = imageSourceOptionDialog
    PhotoPicker(
        modifier = Modifier.clipToBounds(),
        currentPhotoUri = userProfileState.value.fotoPerfil,
        onPickPhoto = {
            imageSourceOptionDialog1 = true
//                            userProfileVM.setFotoPerfil()
        })
}


/**
 * Un DockedSearchBar personalizado que adapta la altura del panel de resultados
 * al número de elementos (vía [contentMaxHeight]) y con un máximo predefinido.
 * Si hay más contenido, el LazyColumn interior hará scroll.
 */
@ExperimentalMaterial3Api
@Composable
fun AdaptiveDockedSearchBar(
    inputField: @Composable () -> Unit,
    expanded: Boolean,
    modifier: Modifier = Modifier,
    colors: SearchBarColors = SearchBarDefaults.colors(),
    tonalElevation: Dp = SearchBarDefaults.TonalElevation,
    shadowElevation: Dp = SearchBarDefaults.ShadowElevation,
    contentMaxHeight: Dp = (screenDetails().heightDp.dp * 0.3f),
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = SearchBarDefaults.dockedShape,
        color = colors.containerColor,
        contentColor = contentColorFor(colors.containerColor),
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
    ) {
        Column {
            inputField()

            AnimatedVisibility(
                visible = expanded,
                enter = DockedEnterTransition,
                exit = DockedExitTransition,
            ) {
                Column(
                    modifier = Modifier.widthIn(min = SearchBarMinWidth)
                        .heightIn(max = contentMaxHeight)
                ) {
                    HorizontalDivider(color = colors.dividerColor)
                    content()
                }
            }
        }
    }
}


val SearchBarMinWidth: Dp = 360.dp

val animationEnterDurationMillis = 600

val animationDelayMillis = 100

val AnimationEnterEasing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)

val AnimationEnterSizeSpec: FiniteAnimationSpec<IntSize> = tween(
    durationMillis = animationEnterDurationMillis,
    delayMillis = animationDelayMillis,
    easing = AnimationEnterEasing,
)

const val AnimationExitDurationMillis = 350.0
val AnimationExitEasing = CubicBezierEasing(0.0f, 1.0f, 0.0f, 1.0f)

val AnimationExitSizeSpec: FiniteAnimationSpec<IntSize> = tween(
    durationMillis = AnimationExitDurationMillis.toInt(),
    delayMillis = animationDelayMillis,
    easing = AnimationExitEasing,
)


val DockedEnterTransition: EnterTransition
    get() = fadeIn(AnimationEnterFloatSpec) + expandVertically(AnimationEnterSizeSpec)
private val DockedExitTransition: ExitTransition
    get() = fadeOut(AnimationExitFloatSpec) + shrinkVertically(AnimationExitSizeSpec)

val AnimationEnterFloatSpec: FiniteAnimationSpec<Float> = tween(
    durationMillis = animationEnterDurationMillis,
    delayMillis = animationDelayMillis,
    easing = AnimationEnterEasing,
)


val AnimationExitFloatSpec: FiniteAnimationSpec<Float> = tween(
    durationMillis = AnimationExitDurationMillis.toInt(),
    delayMillis = animationDelayMillis,
    easing = AnimationExitEasing,
)

@Composable
fun HeaderOnBoardingPages(logo: Painter) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.weight(2f).offset(y = (-12).dp, x = (-40).dp)
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

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview
@Composable
fun Preview_OnboardingStep2() {
    val wsc = WindowSizeClass.calculateFromSize(
        DpSize(400.dp, 800.dp)
    )
    println("WindowSizeClass.widthSizeClass ${wsc.widthSizeClass} ++++++++++++++++++++  WindowSizeClass.widthSizeClass ${wsc.heightSizeClass}")
    OnboardingStep2(logo = painterResource(Res.drawable.compose_multiplatform))
}