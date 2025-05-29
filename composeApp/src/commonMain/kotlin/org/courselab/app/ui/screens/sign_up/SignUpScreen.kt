package org.courselab.app.ui.screens.sign_up


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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.courselab.app.ui.screens.sign_in.composables.FormScaffold
import org.courselab.app.ui.screens.sign_in.composables.GradientScaffold
import org.courselab.app.ui.theme.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Preview
@Composable
fun SignUpScreen(
    logo: Painter?,
    onSignUpComplete: (Boolean) -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
) {
    val signUpViewModel = koinInject<SignUpViewModel>()

    val state by signUpViewModel.signUpState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var lastSuccess by remember { mutableStateOf(false) }
    val isLoading by signUpViewModel.isLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        signUpViewModel.snackbarMsg.collect { message ->
            snackbarHostState.showSnackbar(message)
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
                    modifier = Modifier.size(180.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            FormScaffold(
                fields = listOf(
                    "E-mail" to { signUpViewModel.onSignUpInputChanged("E-mail", it) },
                    "Password" to { signUpViewModel.onSignUpInputChanged("Password", it) }
                ),
                fieldValues = listOf(
                    { state.email },
                    { state.password }
                ), onDoneAction = {
                    signUpViewModel.onSignUpFormSubmitted(SignUp(state)) { success ->
                        lastSuccess = success; showDialog = true; onSignUpComplete(success)
                    }
                }
            )
            Button(
                onClick = {
                    signUpViewModel.onSignUpFormSubmitted(SignUp(state)) { success ->
                        lastSuccess = success; showDialog = true; onSignUpComplete(success)
                    }
                },
                enabled = state.isValid,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Registrando...", color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("SIGN UP", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                border = ButtonDefaults.outlinedButtonBorder().copy(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                )
            ) {
                Text("Volver al Login", color = MaterialTheme.colorScheme.onPrimary)
            }

        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = if (lastSuccess) "Éxito" else "Error",
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (lastSuccess) Color.Black else Color.Red
                )
            },
            text = {
                Text(
                    text = if (lastSuccess) "Usuario creado correctamente" else "Error al crear usuario",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(
                        "OK",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        )
    }
}