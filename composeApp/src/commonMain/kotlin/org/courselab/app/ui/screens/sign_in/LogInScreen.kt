package org.courselab.app.ui.screens.sign_in


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.compose_multiplatform
import org.courselab.app.data.LoginRequest
import org.courselab.app.di.initKoin
import org.courselab.app.ui.theme.BlackPrimary
import org.courselab.app.ui.theme.Rose
import org.courselab.app.ui.theme.YellowLight
import org.courselab.app.ui.theme.YellowPrimary
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Preview()
@Composable
fun LoginScreen(
    logo: Painter? = painterResource(resource = Res.drawable.compose_multiplatform),
    onLoginSuccess: () -> Unit = {},
    onSignUpNavigate: () -> Unit = {},
) {
//    QUITAR initKoin() DESPUÉS DE DISEÑAR LA PANTALLA CON PREVIEW
    initKoin()
//    ------------------------------------------------------------
    val loginViewModel = koinInject<LogInViewModel>()
    val loginState by loginViewModel.loginState.collectAsState()

    var showForgotDialog by remember { mutableStateOf(false) }
    val isLoading by loginViewModel.isLoading.collectAsState()
    var forgotEmail by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        loginViewModel.snackbarMsg.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent,
    ) { padding: PaddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            Color.LightGray,
                            BlackPrimary
                        )
                    )
                ).padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                logo?.let {
                    Image(
                        imageVector = vectorResource(Res.drawable.compose_multiplatform),
                        alignment = Alignment.Center,
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(180.dp)
                            .offset(y = (-80).dp)
                            .shadow(
                                elevation = 40.dp,
                                spotColor = Color.White,
                                ambientColor = Color.Black
                            )
                            .clip(RoundedCornerShape(15.dp))
                            .border(
                                width = 1.dp,
                                color = Color.Transparent,
                                shape = RoundedCornerShape(15.dp)
                            ).zIndex(1f)
                    )
                }
                Spacer(Modifier.height(24.dp))
                OutlinedTextField(
                    value = loginState.email,
                    onValueChange = { loginViewModel.onLoginInputChanged(it, loginState.password) },
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
                    onValueChange = { loginViewModel.onLoginInputChanged(loginState.email, it) },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.Red,
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
                        loginViewModel.onLogInEvent(
                            LoginRequest(
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
                    loginViewModel.onForgotPassword(ForgotPassword(forgotEmail)) {
                    }
                    showForgotDialog = false
                },
                onDismiss = { showForgotDialog = false })
        }
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