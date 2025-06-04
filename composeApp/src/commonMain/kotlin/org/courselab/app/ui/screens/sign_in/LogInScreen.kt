package org.courselab.app.ui.screens.sign_in


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import org.courselab.app.data.LoginRequest
import org.courselab.app.data.UserPreferencesDataStore
import org.courselab.app.ui.screens.sign_in.composables.FormScaffold
import org.courselab.app.ui.screens.sign_in.composables.GradientScaffold
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Preview()
@Composable
fun LoginScreen(
    logo: Painter?,
    onLoginSuccess: () -> Unit,
    onSignUpNavigate: () -> Unit,
) {
    val dataStore: UserPreferencesDataStore = koinInject()
    val shouldDoOnboarding = dataStore.isFirstLogin.collectAsState(initial = false)
    val loginViewModel = koinInject<LogInViewModel>()
    val loginState by loginViewModel.loginState.collectAsState()


    // 1) Leemos el valor actual guardado (“light”, “dark” o “system”)
    val themePref by dataStore.themePreference.collectAsState(initial = "system")

    // 2) isDark arranca siendo true solo si el pref anterior era "dark"
    var isDark by remember { mutableStateOf(themePref == "dark") }

    // 3) Cada vez que isDark cambie, guardamos "dark" o "light" en DataStore
    LaunchedEffect(isDark) {
        dataStore.setThemePreference(if (isDark) "dark" else "light")
    }

//    @Composable
//    fun ThemeToggle(isDark: Boolean) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(8.dp)
//        ) {
//            Text(
//                if (themePref.equals("dark")) "Dark Mode" else "Light Mode",
//                color = MaterialTheme.colorScheme.onSurface
//            )
//            Spacer(Modifier.width(8.dp))
//            Switch(
//                checked = isDark,
//                onCheckedChange = { newValue ->
//                    isDark = newValue
//                }
//            )
//        }
//    }


    var showForgotDialog by remember { mutableStateOf(false) }
    val isLoading by loginViewModel.isLoading.collectAsState()
    var forgotEmail by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        loginViewModel.snackbarMsg.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    GradientScaffold(
        snackbarHostState = snackbarHostState,
    ) { it ->
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            logo?.let {
                Image(
                    it,
                    contentDescription = "Logo",
                    modifier = Modifier.size(180.dp).offset(y = (-50).dp)
                        .clip(RoundedCornerShape(15.dp))
                )
            }
            FormScaffold(
                fields = listOf(
                    "E-mail" to { loginViewModel.onLoginInputChanged(it, loginState.password) },
                    "Password" to { loginViewModel.onLoginInputChanged(loginState.email, it) }
                ),
                onDoneAction = {
                    loginViewModel.onLogInEvent(
                        LoginRequest(
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
                },
                fieldValues = listOf(
                    { loginState.email },
                    { loginState.password }
                )
            )
            Button(
                onClick = {
                    loginViewModel.onLogInEvent(
                        LoginRequest(
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
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Ingresando...", color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(
                        "Log In",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onSignUpNavigate,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    "Sign Up",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = { showForgotDialog = true }) {
                Text(
                    "¿Has olvidado tu contraseña?",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            ToggleButton(isDark, onCheckedChange = { isDark = it })


            Spacer(Modifier.height(16.dp))
        }
    }

    if (showForgotDialog) ForgotPasswordDialog(
        initialEmail = forgotEmail,
        onEmailChange = { forgotEmail = it },
        onSend = {
            loginViewModel.onForgotPassword(ForgotPassword(forgotEmail)) {}
            showForgotDialog = false
        },
        onDismiss = { showForgotDialog = false }
    )
}

@Composable
fun ToggleButton(dark: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = if (dark) "Dark Mode" else "Light Mode",
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.width(8.dp))
        Switch(
            checked = dark,
            onCheckedChange = onCheckedChange
        )
    }
}


@Preview()
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
        title = { Text("Recuperar contraseña", color = MaterialTheme.colorScheme.onPrimary) },
        text = {
            OutlinedTextField(
                value = initialEmail,
                onValueChange = onEmailChange,
                label = { Text("E-mail", color = MaterialTheme.colorScheme.onPrimary) },
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
                Text("Enviar", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    "Cancelar",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        })
}


/**
 * Contenedor genérico para formularios:
 * • Tarjeta con gradiente de fondo según modo (light/dark).
 * • Esquinas extra redondeadas.
 * • Slot para ilustración/icono, título y contenido inyectable.
 * • Botón de acción fuera de scope (se inyecta con content).
 */
@Preview
@Composable
fun FormContainer(
    title: String = "",
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(color = Color.Transparent)
            .padding(vertical = 20.dp, horizontal = 20.dp)
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