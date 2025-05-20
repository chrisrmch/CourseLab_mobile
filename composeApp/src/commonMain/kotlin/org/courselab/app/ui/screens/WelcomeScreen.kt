package org.courselab.app.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.courselab.app.ui.theme.*
import org.courselab.app.viewmodel.AuthViewModel
import org.courselab.app.viewmodel.AuthEvent


@Composable
fun WelcomeScreen(
    logo: Painter,
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onSignUpNavigate: () -> Unit
) {
    val loginState by authViewModel.loginState.collectAsState()

    var showForgotDialog by remember { mutableStateOf(false) }
    val isLoading by authViewModel.isLoading.collectAsState()
    var forgotEmail by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        authViewModel.snackbarMsg.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent
    ) { padding: PaddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(colors = listOf(Rose, BlackPrimary))
                ).padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = logo,
                    contentDescription = "Logo",
                    modifier = Modifier.size(120.dp)
                )
                Spacer(Modifier.height(24.dp))
                OutlinedTextField(
                    value = loginState.email,
                    onValueChange = { authViewModel.onLoginInputChanged(it, loginState.password) },
                    label = { Text("E-mail") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = YellowPrimary,
                        unfocusedBorderColor = YellowLight,
                        cursorColor = YellowPrimary,
                        focusedLabelColor = YellowPrimary,
                        unfocusedLabelColor = YellowLight
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = loginState.password,
                    onValueChange = { authViewModel.onLoginInputChanged(loginState.email, it) },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = YellowPrimary,
                        unfocusedBorderColor = YellowLight,
                        cursorColor = YellowPrimary,
                        focusedLabelColor = YellowPrimary,
                        unfocusedLabelColor = YellowLight
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        authViewModel.handleEvent(
                            AuthEvent.Login(
                                loginState.email, loginState.password
                            )
                        ) { success -> if (success) onLoginSuccess() }
                    },
                    enabled = loginState.isValid && !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = YellowPrimary)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = BlackPrimary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Ingresando...", color = BlackPrimary)
                    } else {
                        Text("Log In", color = BlackPrimary)
                    }
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onSignUpNavigate,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Rose)
                ) { Text("Sign Up", color = BlackPrimary) }
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = { showForgotDialog = true }) {
                    Text("¿Has olvidado tu contraseña?", color = YellowLight)
                }
            }
            if (showForgotDialog) ForgotPasswordDialog(
                initialEmail = forgotEmail,
                onEmailChange = { forgotEmail = it },
                onSend = {
                    authViewModel.handleEvent(AuthEvent.ForgotPassword(forgotEmail)) {

                    }
                    showForgotDialog = false
                },
                onDismiss = { showForgotDialog = false })
        }
    }
}

@Composable
fun ForgotPasswordDialog(
    initialEmail: String, onEmailChange: (String) -> Unit, onSend: () -> Unit, onDismiss: () -> Unit
) {
    AlertDialog(
        containerColor = BlackPrimary,
        onDismissRequest = onDismiss,
        title = { Text("Recuperar contraseña", color = YellowPrimary) },
        text = {
            OutlinedTextField(
                value = initialEmail,
                onValueChange = onEmailChange,
                label = { Text("E-mail", color = YellowLight) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = YellowLight,
                    unfocusedTextColor = YellowLight,
                    focusedBorderColor = YellowPrimary,
                    unfocusedBorderColor = YellowLight,
                    cursorColor = YellowPrimary,
                    focusedLabelColor = YellowPrimary,
                    unfocusedLabelColor = YellowLight,
                ),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
        },
        confirmButton = {
            Button(
                onClick = onSend,
                colors = ButtonDefaults.buttonColors(containerColor = YellowPrimary)
            ) {
                Text("Enviar", color = BlackPrimary)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Rose)
            ) {
                Text("Cancelar", color = BlackPrimary)
            }
        }
    )
}