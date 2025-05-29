package org.courselab.app.ui.screens.sign_in


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.courselab.app.data.LoginRequest
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Preview()
@Composable
fun LoginScreen(
    logo: Painter?,
    onLoginSuccess: () -> Unit,
    onSignUpNavigate: () -> Unit,
) {
    val loginViewModel = koinInject<LogInViewModel>()
    val loginState by loginViewModel.loginState.collectAsState()
    val colorScheme = MaterialTheme.colorScheme

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
            FormContainer(
                title = "Iniciar Sesión",
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
            ) {
                OutlinedTextField(
                    shape = RoundedCornerShape(20.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    value = loginState.email,
                    onValueChange = { loginViewModel.onLoginInputChanged(it, loginState.password) },
                    label = { Text("E-mail") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colorScheme.onPrimary,
                        unfocusedTextColor = colorScheme.onSurfaceVariant,
                        focusedBorderColor = colorScheme.onBackground,
                        unfocusedBorderColor = colorScheme.onSurfaceVariant,
                        cursorColor = colorScheme.inverseSurface,
                        focusedLabelColor = colorScheme.onPrimary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                )
                Spacer(Modifier.height(8.dp))
                RoundedSecureTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = loginState.password,
                    onValueChange = { loginViewModel.onLoginInputChanged(loginState.email, it) },
                    label = "Contraseña",
                    logInViewModel = loginViewModel,
                    onLoginSuccess = onLoginSuccess,
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        loginViewModel.onLogInEvent(
                            LoginRequest(
                                loginState.email, loginState.password
                            )
                        ) { success -> if (success) onLoginSuccess() }
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
                    Text("¿Has olvidado tu contraseña?", color = MaterialTheme.colorScheme.onPrimary)
                }

            }
        }
        if (showForgotDialog) ForgotPasswordDialog(
            initialEmail = forgotEmail,
            onEmailChange = { forgotEmail = it },
            onSend = {
                loginViewModel.onForgotPassword(ForgotPassword(forgotEmail)) {}
                showForgotDialog = false
            },
            onDismiss = { showForgotDialog = false })
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

@Composable
fun GradientScaffold(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            ).padding(padding)
        ) {
            content(padding)
        }
    }
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RoundedSecureTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    logInViewModel: LogInViewModel,
    onLoginSuccess: () -> Unit,
) {
    val loginState by logInViewModel.loginState.collectAsState()
    val colorScheme = MaterialTheme.colorScheme
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= (value.length + 1)) {
                onValueChange(it)
            }
        },
        label = { Text(label) },
        visualTransformation = PasswordVisualTransformation(mask = '\u2022'),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                logInViewModel.onLogInEvent(
                    LoginRequest(
                        loginState.email, loginState.password
                    )
                ) { success -> if (success) onLoginSuccess() }

            }
        ),
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = colorScheme.onPrimary,
            unfocusedTextColor = colorScheme.onSurfaceVariant,
            focusedBorderColor = colorScheme.onBackground,
            unfocusedBorderColor = colorScheme.onSurfaceVariant,
            cursorColor = colorScheme.inverseSurface,
            focusedLabelColor = colorScheme.onPrimary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        singleLine = true,
        maxLines = 1
    )
}