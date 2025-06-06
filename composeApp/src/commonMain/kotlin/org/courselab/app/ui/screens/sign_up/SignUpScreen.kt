package org.courselab.app.ui.screens.sign_up


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.courselab.app.ui.screens.sign_in.composables.FormField
import org.courselab.app.ui.screens.sign_in.composables.FormScaffold
import org.courselab.app.ui.screens.sign_in.composables.GradientScaffold
import org.courselab.app.ui.screens.sign_in.composables.OutlinedWelcomeButtons
import org.courselab.app.ui.screens.sign_in.composables.ThemeToggle
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { it ->
        Column(
            modifier = Modifier.fillMaxSize().padding(it).padding(16.dp).offset(y = (-26.9).dp),
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
                    FormField("Email", { signUpViewModel.onSignUpInputChanged("E-mail", it) } ),
                    FormField("Password",  { signUpViewModel.onSignUpInputChanged("Password", it) } )
                ),
                fieldValues = listOf(
                    { state.email },
                    { state.password }
                ), onDoneAction = {
                    signUpViewModel.onSignUpFormSubmitted(
                        SignUpRequestDTO(
                            email = state.email,
                            password = state.password
                        )
                    ) { success ->
                        lastSuccess = success; showDialog = true; onSignUpComplete(success)
                    }
                }
            )

            OutlinedWelcomeButtons.Primary(
                onClick = {
                    signUpViewModel.onSignUpFormSubmitted(
                        SignUpRequestDTO(
                            email = state.email,
                            password = state.password
                        )
                    ) { success ->
                        lastSuccess = success; showDialog = true; onSignUpComplete(success)
                    }
                },
                enabled = state.isValid,
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Registrando...", color = MaterialTheme.colorScheme.onSecondary)
                } else {
                    Text(
                        "Sign Up",
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            OutlinedWelcomeButtons.Secondary(
                text = "Already have an account?",
                onClick = onNavigateToLogin,
            )

            ThemeToggle()
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