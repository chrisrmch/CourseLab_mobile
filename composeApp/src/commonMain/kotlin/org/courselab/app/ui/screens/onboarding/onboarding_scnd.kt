@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.connexion_en_cours
import courselab.composeapp.generated.resources.error_search
import courselab.composeapp.generated.resources.hint_search_municipio
import courselab.composeapp.generated.resources.logging_in
import courselab.composeapp.generated.resources.logo
import courselab.composeapp.generated.resources.no_results
import courselab.composeapp.generated.resources.onboarding_button_back
import courselab.composeapp.generated.resources.onboarding_button_finish
import courselab.composeapp.generated.resources.onboarding_complete_final_data
import courselab.composeapp.generated.resources.onboarding_create_your_profile
import courselab.composeapp.generated.resources.search_municipio_placeholder
import kotlinx.coroutines.flow.collectLatest
import org.courselab.app.LocalNavController
import org.courselab.app.PermissionCallback
import org.courselab.app.PermissionStatus
import org.courselab.app.PermissionType
import org.courselab.app.PermissionsManager
import org.courselab.app.Screen
import org.courselab.app.createPermissionsManager
import org.courselab.app.models.MunicipioSearchResult
import org.courselab.app.screenDetails
import org.courselab.app.ui.screens.onboarding.composables.RequestDetailsCard
import org.courselab.app.ui.screens.onboarding.dto.OnboardingUiState
import org.courselab.app.ui.screens.onboarding.viewModel.MunicipioSearchViewModel
import org.courselab.app.ui.screens.onboarding.viewModel.UserProfileViewModel
import org.courselab.app.ui.screens.sign_in.composables.GradientScaffold
import org.courselab.app.ui.screens.sign_in.composables.OutlinedWelcomeButtons
import org.courselab.app.ui.screens.sign_in.composables.ThemeToggle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject


@Composable
fun UserProfileDetailsScreenSecond(
    logo: Painter,
) {
    val coroutineScope = rememberCoroutineScope()

    var launchSetting by remember { mutableStateOf(value = false) }
    var permissionRationalDialog by remember { mutableStateOf(value = false) }

    val userProfileVM = koinInject<UserProfileViewModel>()
    val uploadState by userProfileVM.uploadState.collectAsState()


    //--------------------------------------------------------------------------------
    var showLoadingScreen by remember { mutableStateOf(false) }
    val navController = LocalNavController.current
    val profile = userProfileVM.userState.collectAsState()
    val searchViewModel: MunicipioSearchViewModel = koinInject()
    LaunchedEffect(Unit) { searchViewModel.initialize() }
    val query by searchViewModel.inputText.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val viewState by searchViewModel.viewState.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    val isFormValid = profile.value.municipio.isNotBlank()

    val items = when (viewState) {
        is MunicipioSearchViewModel.ViewState.MunicipioSearchResultFetch -> (viewState as MunicipioSearchViewModel.ViewState.MunicipioSearchResultFetch).municipios
        else -> emptyList()
    }

    LaunchedEffect(query.isEmpty()) {
        if (query.isEmpty()) focusRequester.requestFocus()
    }

    LaunchedEffect(Unit) {
        userProfileVM.uploadState.collectLatest { state ->
            when (state) {
                is OnboardingUiState.Loading -> {
                    showLoadingScreen = true;
                }

                is OnboardingUiState.Success -> {
                    navController?.navigate(Screen.HomeScreen) {
                        navController.graph.id.let {
                            popUpTo(it) {
                                inclusive = true
                            }
                        }
                        launchSingleTop = true
                    }
                }

                is OnboardingUiState.Error -> {}
                OnboardingUiState.Idle -> Unit
            }
        }
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
            modifier = Modifier.fillMaxSize().padding(paddingValues)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            HeaderOnBoardingPages(logo)
            Spacer(modifier = Modifier.height(10.dp))

            RequestDetailsCard(modifier = Modifier) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Spacer(modifier = Modifier.height(10.dp))
                    AdaptiveDockedSearchBar(
                        inputField = {
                            val displayText = profile.value.municipio.ifEmpty { query }
                            val locationSelected = profile.value.municipio.isNotBlank()
                            val fieldEnabled = !locationSelected || expanded
                            SearchInputField(
                                text = displayText,
                                placeholder = stringResource(Res.string.search_municipio_placeholder),
                                onValueChange = {
                                    searchViewModel.updateInput(it)
                                    if (!expanded) expanded = true
                                },
                                onClear = {
                                    userProfileVM.setUbicacion("")
                                    userProfileVM.setMunicipioID(0)
                                    searchViewModel.revertToInitialState()
                                    expanded = true
                                    searchViewModel.clearInput()
                                },
                                showClear = query.isNotBlank() || locationSelected,
                                enabled = profile.value.municipio.isBlank() || expanded,
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
                        when (viewState) {
                            MunicipioSearchViewModel.ViewState.Loading -> LoadingIndicator()
                            MunicipioSearchViewModel.ViewState.NoResults -> NoResultsMessage()
                            is MunicipioSearchViewModel.ViewState.MunicipioSearchResultFetch -> {
                                SearchResultsList(
                                    items = (viewState as MunicipioSearchViewModel.ViewState.MunicipioSearchResultFetch).municipios,
                                    onItemClick = { municipio ->
                                        userProfileVM.setMunicipio(municipio)
                                        searchViewModel.revertToInitialState()
                                        expanded = false
                                    }
                                )
                            }

                            MunicipioSearchViewModel.ViewState.Error -> ErrorMessage()
                            else -> HintMessage(stringResource(Res.string.hint_search_municipio))
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(12.dp)
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
                            onClick = {
                                userProfileVM.onFinishOnboarding()
                            },
                            enabled = isFormValid,
                            modifier = Modifier.weight(1f)
                        ) {
                            if (showLoadingScreen) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(stringResource(Res.string.connexion_en_cours), color = MaterialTheme.colorScheme.onSecondary)
                            } else {
                                Text(
                                    text = stringResource(Res.string.onboarding_button_finish),
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }

    }
}

@Composable
fun fetchActualPermissionManager(
    launchCamera: (Boolean) -> Unit,
    launchGallery: (Boolean) -> Unit,
    permissionRationalDialog: (Boolean) -> Unit
): PermissionsManager {
    return createPermissionsManager(
        object : PermissionCallback {
            override fun onPermissionStatus(
                permissionType: PermissionType,
                status: PermissionStatus,
            ) {
                when (status) {
                    PermissionStatus.GRANTED -> {
                        when (permissionType) {
                            PermissionType.CAMERA -> launchCamera(true)
                            PermissionType.GALLERY -> launchGallery(true)
                        }
                    }

                    else -> {
                        permissionRationalDialog(true)
                    }
                }
            }
        }
    )
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
    val focusRequester = remember { FocusRequester() }

    SearchBarDefaults.InputField(
        query = text,
        onQueryChange = { if (enabled) onValueChange(it) },
        onSearch = { },
        expanded = true,
        onExpandedChange = { },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .clickable {
                if (!enabled) {
                    onClear()
                    focusRequester.requestFocus()
                }
            },
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
    items: List<MunicipioSearchResult>, onItemClick: (MunicipioSearchResult) -> Unit
) {
    LazyColumn {
        items(items, key = { it.municipio_ID }) { municipio ->
            ListItem(
                headlineContent = { Text("${municipio.municipio} / ${municipio.provincia}") },
                modifier = Modifier.fillMaxWidth().clickable { onItemClick(municipio) })
            HorizontalDivider()
        }
    }
}

@Composable
private fun LoadingIndicator() = Box(
    Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center
) { CircularProgressIndicator() }

@Composable
private fun NoResultsMessage() = Text(
    stringResource(Res.string.no_results),
    Modifier.fillMaxWidth().padding(16.dp),
    color = MaterialTheme.colorScheme.error
)

@Composable
private fun ErrorMessage() = Text(
    stringResource(Res.string.error_search),
    Modifier.fillMaxWidth().padding(16.dp),
    color = MaterialTheme.colorScheme.error
)

@Composable
private fun HintMessage(text: String) = Text(
    text, Modifier.fillMaxWidth().padding(16.dp), color = MaterialTheme.colorScheme.onSurfaceVariant
)

@Composable
private fun CustomInputFieldColors() = TextFieldDefaults.colors(
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