package org.courselab.app.ui.screens.log_in


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
import org.courselab.app.data.LoginRequestDTO
import org.courselab.app.data.UserPreferencesDataStore
import org.courselab.app.org.courselab.app.LocalAppLocalization
import org.courselab.app.org.courselab.app.LocalUrlLauncher
import org.courselab.app.org.courselab.app.ui.screens.log_in.dto.ForgotPasswordDTO
import org.courselab.app.org.courselab.app.ui.screens.log_in.dto.LoginFormState
import org.courselab.app.ui.screens.log_in.composables.FormField
import org.courselab.app.ui.screens.log_in.composables.FormScaffold
import org.courselab.app.ui.screens.log_in.composables.GradientScaffold
import org.courselab.app.ui.screens.log_in.composables.OutlinedWelcomeButtons
import org.courselab.app.ui.screens.log_in.composables.ThemeToggle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Preview
@Composable
fun LoginScreen(
    logo: Painter,
    onLoginSuccess: () -> Unit,
    onSignUpNavigate: () -> Unit,
    dataStore: UserPreferencesDataStore = koinInject(),
) {
    val shouldDoOnboarding by dataStore.isFirstLogin.collectAsState(initial = false)

    val lang_pref by dataStore.languagePreference.collectAsState(initial = LocalAppLocalization.current.code)

    val loginViewModel = koinInject<LogInViewModel>()
    val loginState by loginViewModel.loginState.collectAsState()
    val isLoading by loginViewModel.isLoading.collectAsState()

    var showForgotDialog by remember { mutableStateOf(false) }
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
            modifier = Modifier.padding(it).fillMaxSize().padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = logo,
                contentDescription = stringResource(Res.string.logo),
                modifier = Modifier.size(180.dp).offset(y = (-50).dp)
                    .clip(RoundedCornerShape(15.dp))
            )

            Button(
                modifier = Modifier,
                onClick = {
                    urlLauncher?.openAppSettings()
                }
            ) {
                Text(stringResource(Res.string.change_lang))
            }

            FormScaffold(
                fields = listOf(
                    FormField(
                        stringResource(
                            Res.string.email,
                        ), { loginViewModel.onLoginInputChanged(it, loginState.password) }),
                    FormField(
                        stringResource(Res.string.password),
                        { loginViewModel.onLoginInputChanged(loginState.email, it) })
                ),
                fieldValues = listOf({ loginState.email }, { loginState.password }),
                onDoneAction = {
                    loginViewModel.onLogInEvent(
                        LoginRequestDTO(
                            loginState.email, loginState.password
                        ),
                    ) { success, firstLogIn ->
                        println("SUCCESS: $success, FIRST LOGIN: $firstLogIn")
                        if (success && !firstLogIn) {
                            println("se ha llegado a NAVCONTROLLER TO HOMESCREEN 1")
                            onLoginSuccess()
                        } else if (success && firstLogIn) {
                            println("se ha llegado a NAVCONTROLLER TO HOMESCREEN 2")
                        } else {
                            println("se ha llegado a NAVCONTROLLER TO HOMESCREEN 3")
                        }
                    }
                }
            )
            OutlinedWelcomeButtons.Primary(
                onClick = {
                    loginViewModel.onLogInEvent(
                        LoginRequestDTO(
                            loginState.email, loginState.password
                        )
                    ) { success, firstLogIn ->
                        if (success && !firstLogIn) {
                            println("se ha llegado a NAVCONTROLLER TO HOMESCREEN 1")
                            onLoginSuccess()
                        } else if (success && firstLogIn) {
                            println("se ha llegado a NAVCONTROLLER TO HOMESCREEN 2")
                        } else {
                            println("se ha llegado a NAVCONTROLLER TO HOMESCREEN 3")
                        }
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
            Spacer(Modifier.height(8.dp))
            OutlinedWelcomeButtons.Secondary(
                text = stringResource(Res.string.sign_up),
                onClick = onSignUpNavigate,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = { showForgotDialog = true }) {
                Text(
                    stringResource(Res.string.forgot_password),
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
            ThemeToggle()
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
        onDismiss = { showForgotDialog = false })
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
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        onDismissRequest = onDismiss,
        title = {
            Text(
                stringResource(Res.string.recover_password),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        text = {
            OutlinedTextField(
                shape = RoundedCornerShape(15.dp),
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
                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.tertiary,
                ),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
        },

        confirmButton = {
            ElevatedButton(
                onClick = onSend,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(stringResource(Res.string.send), color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        dismissButton = {
            ElevatedButton(
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