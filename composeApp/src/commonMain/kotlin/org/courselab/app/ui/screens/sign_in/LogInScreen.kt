package org.courselab.app.ui.screens.sign_in


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.change_lang
import courselab.composeapp.generated.resources.email
import courselab.composeapp.generated.resources.forgot_password
import courselab.composeapp.generated.resources.logging_in
import courselab.composeapp.generated.resources.login
import courselab.composeapp.generated.resources.logo
import courselab.composeapp.generated.resources.password
import courselab.composeapp.generated.resources.sign_up
import courselab.composeapp.generated.resources.snackbar_recovery_link_sent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.courselab.app.LocalNavController
import org.courselab.app.LocalUrlLauncher
import org.courselab.app.Screen
import org.courselab.app.UrlLauncher
import org.courselab.app.data.repository.LoginRequestDTO
import org.courselab.app.ui.screens.sign_in.composables.ForgotPasswordDialog
import org.courselab.app.ui.screens.sign_in.composables.FormField
import org.courselab.app.ui.screens.sign_in.composables.FormScaffold
import org.courselab.app.ui.screens.sign_in.composables.GradientScaffold
import org.courselab.app.ui.screens.sign_in.composables.OutlinedWelcomeButtons
import org.courselab.app.ui.screens.sign_in.composables.ThemeToggle
import org.courselab.app.ui.screens.sign_in.dto.ForgotPasswordDTO
import org.courselab.app.ui.screens.sign_in.dto.LogInFormStateDTO
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun LoginScreen(
    logo: Painter?,
    onSignUpNavigate: () -> Unit,
) {
    val navController = LocalNavController.current
    val loginViewModel = koinInject<LogInViewModel>()
    val loginState by loginViewModel.loginState.collectAsState()
    val isLoading by loginViewModel.isLoading.collectAsState()
    //-------------------------------------------------------------------- ESTADO DE LA PANTALLA ----------
    var showForgotDialog by remember { mutableStateOf(false) }
    var forgotEmail by remember { mutableStateOf("") }
    val snackbarMessages = remember { SnackbarHostState() }
    val recoveryLinkSent = stringResource(Res.string.snackbar_recovery_link_sent)

    LaunchedEffect(Unit) {
        loginViewModel.snackbarMsg.collect { msg ->
            snackbarMessages.showSnackbar(msg)
        }
    }

    GradientScaffold(
        snackbarHost = { SnackbarHost(snackbarMessages) },
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).windowInsetsPadding(WindowInsets.Companion.ime),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            logoInsideLogIn(logo)
            LogInForm(loginViewModel, loginState, navController)
            LogInButton(loginViewModel, loginState, navController, isLoading)
            Spacer(Modifier.height(8.dp))
            SignUpButton(onSignUpNavigate)
            Spacer(Modifier.height(8.dp))
            ForgotPasswordTextButton { it: Boolean -> showForgotDialog = it }
        }
    }

    if (showForgotDialog) ForgotPasswordDialog(
        initialEmail = forgotEmail,
        onEmailChange = { forgotEmail = it },
        onSend = {
            loginViewModel.onForgotPassword(
                ForgotPasswordDTO(forgotEmail),
                onResult = {
                    if (it) {
                        showForgotDialog = false
                    }
                },
                recoveryLinkSent = recoveryLinkSent
            )
        },
        onDismiss = { showForgotDialog = false }
    )
}



@Composable
private fun LogInForm(
    loginViewModel: LogInViewModel,
    loginState: LogInFormStateDTO,
    navController: NavHostController?
) {
    FormScaffold(
        fields = listOf(
            FormField(
                stringResource(Res.string.email)
            ) { it: String -> loginViewModel.onLoginInputChanged(it, loginState.password) },
            FormField(
                stringResource(Res.string.password)
            ) { it: String -> loginViewModel.onLoginInputChanged(loginState.email, it) }
        ),
        fieldValues = listOf({ loginState.email }, { loginState.password }),
        onDoneAction = {
            loginViewModel.onLogInEvent(
                LoginRequestDTO(email = loginState.email, password = loginState.password),
            ) { success, firstLogIn ->
                handleNavigation(success, firstLogIn, navController!!, loginViewModel.scope)
            }
        }
    )
}

@Composable
private fun ForgotPasswordTextButton(canShow: (Boolean) -> Unit) {
    TextButton(onClick = { canShow(true) }) {
        Text(
            stringResource(Res.string.forgot_password),
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
private fun SignUpButton(onSignUpNavigate: () -> Unit) {
    OutlinedWelcomeButtons.Secondary(
        text = stringResource(Res.string.sign_up),
        onClick = onSignUpNavigate,
        modifier = Modifier.fillMaxWidth(),
    )
}


@Composable
private fun LogInButton(
    loginViewModel: LogInViewModel,
    loginState: LogInFormStateDTO,
    navController: NavHostController?,
    isLoading: Boolean
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedWelcomeButtons.Primary(
        onClick = {
            keyboardController?.hide()
            loginViewModel.onLogInEvent(
                LoginRequestDTO(email = loginState.email, password = loginState.password),
            ) { success, firstLogIn ->
                handleNavigation(success, firstLogIn, navController!!, loginViewModel.scope)
            }
        },
        enabled = loginState.isValid && !isLoading,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(Modifier.width(8.dp))
            Text(stringResource(Res.string.logging_in), color = MaterialTheme.colorScheme.onSecondary)
        } else {
            Text(
                stringResource(Res.string.login),
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun logoInsideLogIn(logo: Painter?) {
    logo?.let {
        Image(
            it,
            contentDescription = stringResource(Res.string.logo),
            modifier = Modifier.size(160.dp).offset(y = (-50).dp)
                .clip(RoundedCornerShape(15.dp))
        )
    }
}

private fun handleNavigation(
    success: Boolean,
    isFirstLogin: Boolean,
    navController: NavController,
    scope: CoroutineScope
) {
    if (success && !isFirstLogin) {
        scope.launch {
            delay(1500)
            navController.navigate(Screen.HomeScreen)
        }
    } else if (success && isFirstLogin) {
        scope.launch {
            delay(1500)
            navController.navigate(Screen.FirstOnboardingScreen)
        }
    }
}