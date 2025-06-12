package org.courselab.app.ui.screens.sign_in


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.cancel
import courselab.composeapp.generated.resources.change_lang
import courselab.composeapp.generated.resources.email
import courselab.composeapp.generated.resources.forgot_password
import courselab.composeapp.generated.resources.login
import courselab.composeapp.generated.resources.logo
import courselab.composeapp.generated.resources.password
import courselab.composeapp.generated.resources.recover_password
import courselab.composeapp.generated.resources.send
import courselab.composeapp.generated.resources.sign_up
import courselab.composeapp.generated.resources.snackbar_recovery_link_sent
import org.courselab.app.UrlLauncher
import org.courselab.app.data.LoginRequestDTO
import org.courselab.app.org.courselab.app.FirstOnboardingScreen
import org.courselab.app.org.courselab.app.HomeScreen
import org.courselab.app.org.courselab.app.LocalAppLocalization
import org.courselab.app.org.courselab.app.LocalNavController
import org.courselab.app.org.courselab.app.LocalUrlLauncher
import org.courselab.app.ui.screens.sign_in.composables.FormField
import org.courselab.app.ui.screens.sign_in.composables.FormScaffold
import org.courselab.app.ui.screens.sign_in.composables.GradientScaffold
import org.courselab.app.ui.screens.sign_in.composables.OutlinedWelcomeButtons
import org.courselab.app.ui.screens.sign_in.composables.ThemeToggle
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun LoginScreen(
    logo: Painter?,
    onSignUpNavigate: () -> Unit,
) {
    val navController = LocalNavController.current

    val loginViewModel = koinInject<LogInViewModel>()
    val loginState by loginViewModel.loginState.collectAsState()
    var showForgotDialog by remember { mutableStateOf(false) }
    val isLoading by loginViewModel.isLoading.collectAsState()
    var forgotEmail by remember { mutableStateOf("") }
    val snackbarMessages = remember { SnackbarHostState() }

    val urlLauncher = LocalUrlLauncher.current

    val recoveryLinkSent = stringResource(Res.string.snackbar_recovery_link_sent)

    LaunchedEffect(Unit) {
        loginViewModel.snackbarMsg.collect { msg ->
            snackbarMessages.showSnackbar(msg)
        }
    }

    GradientScaffold(
        snackbarHost = { SnackbarHost(snackbarMessages) },
    ) { it ->
        Column(
            modifier = Modifier.fillMaxSize().padding(it).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            logoInsideLogIn(logo)
            ChangeLanguageButton(urlLauncher)
            LogInForm(loginViewModel, loginState, navController)
            LogInButton(loginViewModel, loginState, navController, isLoading)
            Spacer(Modifier.height(8.dp))
            SignUpButton(onSignUpNavigate)
            Spacer(Modifier.height(8.dp))
            ForgotPassworTextButton(showForgotDialog)
            ThemeToggle()
        }
    }

    if (showForgotDialog) ForgotPasswordDialog(
        initialEmail = forgotEmail,
        onEmailChange = { forgotEmail = it },
        onSend = {
            loginViewModel.onForgotPassword(
                ForgotPassword(forgotEmail),
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
private fun ChangeLanguageButton(urlLauncher: UrlLauncher?) {
    Button(
        modifier = Modifier,
        onClick = {
            urlLauncher?.openAppSettings()
        }
    ) {
        Text(stringResource(Res.string.change_lang))
    }
}

@Composable
private fun LogInForm(
    loginViewModel: LogInViewModel,
    loginState: LoginFormState,
    navController: NavHostController?
) {
    FormScaffold(
        fields = listOf(
            FormField(
                stringResource(
                    Res.string.email,
                    stringResource(LocalAppLocalization.current.stringRes)
                ), { loginViewModel.onLoginInputChanged(it, loginState.password) }),
            FormField(
                stringResource(Res.string.password),
                { loginViewModel.onLoginInputChanged(loginState.email, it) })
        ),
        fieldValues = listOf({ loginState.email }, { loginState.password }),
        onDoneAction = {
            loginViewModel.onLogInEvent(
                LoginRequestDTO(email = loginState.email, password = loginState.password),
            ) { success, firstLogIn ->
                handleNavigation(success, firstLogIn, navController!!)
            }
        }
    )
}

@Composable
private fun ForgotPassworTextButton(showForgotDialog: Boolean) {
    var showForgotDialog1 = showForgotDialog
    TextButton(onClick = { showForgotDialog1 = true }) {
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
    loginState: LoginFormState,
    navController: NavHostController?,
    isLoading: Boolean
) {
    OutlinedWelcomeButtons.Primary(
        onClick = {
            loginViewModel.onLogInEvent(
                LoginRequestDTO(
                    loginState.email, loginState.password
                )
            ) { success, firstLogIn ->
                handleNavigation(success, firstLogIn, navController!!)
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
            Text("Ingresando...", color = MaterialTheme.colorScheme.onSecondary)
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
            modifier = Modifier.size(180.dp).offset(y = (-50).dp)
                .clip(RoundedCornerShape(15.dp))
        )
    }
}


@Preview
@Composable
fun ForgotPasswordDialog(
    initialEmail: String = "",
    onEmailChange: (String) -> Unit = {},
    onSend: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.primary,
        onDismissRequest = onDismiss,
        title = {
            Text(
                stringResource(Res.string.recover_password),
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        text = {
            OutlinedTextField(
                value = initialEmail,
                onValueChange = onEmailChange,
                label = {
                    Text(
                        stringResource(Res.string.email),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSecondary,
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    cursorColor = MaterialTheme.colorScheme.tertiary,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.tertiary,
                ),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
        },

        confirmButton = {
            Button(
                onClick = onSend,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(stringResource(Res.string.send), color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    stringResource(Res.string.cancel),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        })
}

@Preview
@Composable
fun FormContainer(
    title: String = "",
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    Column(
        modifier.fillMaxWidth().padding(16.dp).clip(RoundedCornerShape(25.dp))
            .background(color = Color.Transparent).padding(vertical = 20.dp, horizontal = 20.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(20.dp))
        content()
    }
}

private fun handleNavigation(
    success: Boolean,
    isFirstLogin: Boolean,
    navController: NavController
) {
    println("SUCCESS: $success, FIRST LOGIN: $isFirstLogin")
    if (success && !isFirstLogin) {
        navController.navigate(HomeScreen)
    } else if (success && isFirstLogin) {
        navController.navigate(FirstOnboardingScreen)
    } else {
        print(" SUCCESS $success %% FIRST LOGIN $isFirstLogin")
    }
}