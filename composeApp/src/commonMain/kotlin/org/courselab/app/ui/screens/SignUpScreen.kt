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
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.courselab.app.ui.theme.*
import org.courselab.app.viewmodel.AuthEvent
import org.courselab.app.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(
    logo: Painter?,
    authViewModel: AuthViewModel,
    onSignUpComplete: (Boolean) -> Unit
) {
    val state by authViewModel.signUpState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var lastSuccess by remember { mutableStateOf(false) }
    val isLoading by authViewModel.isLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        authViewModel.snackbarMsg.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color.Transparent

    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(colors = listOf(Rose, BlackPrimary))
                ).padding(paddingValues = padding)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                logo?.let { Image(it, contentDescription = "Logo", modifier = Modifier.size(100.dp)) }
                Spacer(Modifier.height(16.dp))
                listOf(
                    "Nombre" to state.nombre,
                    "Apellidos" to state.apellidos,
                    "E-mail" to state.email,
                    "Contraseña" to state.password,
                    "Fecha de nacimiento" to state.fechaNacimiento,
                    "Género" to state.genero
                ).forEach { (label, value) ->
                    OutlinedTextField(
                        value = value,
                        onValueChange = {
                            authViewModel.onSignUpInputChanged(
                                label.lowercase(),
                                it
                            )
                        },
                        label = { Text(label) },
                        enabled = !isLoading,
                        visualTransformation = if (label == "Contraseña") PasswordVisualTransformation() else VisualTransformation.None,
                        keyboardOptions = if (label == "E-mail") KeyboardOptions(keyboardType = KeyboardType.Email) else KeyboardOptions.Default,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        authViewModel.handleEvent(AuthEvent.SignUp(state)) { success ->
                            lastSuccess = success; showDialog = true; onSignUpComplete(success)
                        }
                    },
                    enabled = state.isValid,
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
                        Text("Registrando...", color = BlackPrimary)
                    } else {
                        Text("SIGN UP", color = BlackPrimary)
                    }
                }
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
                        color = YellowPrimary
                    )
                }
            }
        )
    }
}