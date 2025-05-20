//// File: commonMain/kotlin/org/courselab/app/ui/theme/Color.kt
//package org.courselab.app.ui.theme
//
//import androidx.compose.ui.graphics.Color
//
//val YellowPrimary = Color(0xFFFFCF00)
//val YellowLight = Color(0xFFF8EAB3)
//val Rose = Color(0xFFB59898)
//val BlackPrimary = Color(0xFF000000)
//val RedPrimary = Color(0xFFD32F2F)
//val RedLight = Color(0xFFE57373)
//
//// File: commonMain/kotlin/org/courselab/app/ui/theme/Theme.kt
//package org.courselab.app.ui.theme
//
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.darkColorScheme
//import androidx.compose.material3.lightColorScheme
//import androidx.compose.runtime.Composable
//
//private val LightColors = lightColorScheme(
//    primary = YellowPrimary,
//    onPrimary = BlackPrimary,
//    primaryContainer = YellowLight,
//    onPrimaryContainer = BlackPrimary,
//    secondary = Rose,
//    onSecondary = BlackPrimary,
//    background = BlackPrimary,
//    onBackground = YellowLight,
//    surface = BlackPrimary,
//    onSurface = YellowLight,
//    error = RedPrimary,
//    onError = Color.White
//)
//
//private val DarkColors = darkColorScheme(
//    primary = YellowLight,
//    onPrimary = BlackPrimary,
//    primaryContainer = YellowPrimary,
//    onPrimaryContainer = BlackPrimary,
//    secondary = Rose,
//    onSecondary = BlackPrimary,
//    background = BlackPrimary,
//    onBackground = YellowLight,
//    surface = BlackPrimary,
//    onSurface = YellowLight,
//    error = RedLight,
//    onError = Color.White
//)
//
//val appTypography2 = Typography()
//
//@Composable
//fun AppTheme(
//    useDarkTheme: Boolean = isSystemInDarkTheme(),
//    content: @Composable () -> Unit
//) {
//    MaterialTheme(
//        colorScheme = if (useDarkTheme) DarkColors else LightColors,
//        typography = appTypography2,
//        content = content
//    )
//}
//
//// File: commonMain/kotlin/org/courselab/app/ui/screens/WelcomeScreen.kt
//package org.courselab.app.ui.screens
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.unit.dp
//import courselab.composeapp.generated.resources.Res
//import org.courselab.app.ui.theme.*
//
//@Composable
//fun WelcomeScreen(
//    authViewModel: AuthViewModel,
//    onLoginSuccess: () -> Unit,
//    onSignUpNavigate: () -> Unit
//) {
//    val loginState by authViewModel.loginState.collectAsState()
//    var showForgotDialog by remember { mutableStateOf(false) }
//    var forgotEmail by remember { mutableStateOf("") }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(colors = listOf(Rose, BlackPrimary))
//            )
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Image(
//                painter = painterResource(Res.drawable.logo),
//                contentDescription = "Logo",
//                modifier = Modifier.size(120.dp)
//            )
//            Spacer(Modifier.height(24.dp))
//            OutlinedTextField(
//                value = loginState.email,
//                onValueChange = { authViewModel.onLoginInputChanged(it, loginState.password) },
//                label = { Text("E-mail") },
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    textColor = Color.White,
//                    focusedBorderColor = YellowPrimary,
//                    unfocusedBorderColor = YellowLight,
//                    cursorColor = YellowPrimary,
//                    focusedLabelColor = YellowPrimary,
//                    unfocusedLabelColor = YellowLight
//                ),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
//                modifier = Modifier.fillMaxWidth()
//            )
//            Spacer(Modifier.height(8.dp))
//            OutlinedTextField(
//                value = loginState.password,
//                onValueChange = { authViewModel.onLoginInputChanged(loginState.email, it) },
//                label = { Text("Contraseña") },
//                visualTransformation = PasswordVisualTransformation(),
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    textColor = Color.White,
//                    focusedBorderColor = YellowPrimary,
//                    unfocusedBorderColor = YellowLight,
//                    cursorColor = YellowPrimary,
//                    focusedLabelColor = YellowPrimary,
//                    unfocusedLabelColor = YellowLight
//                ),
//                modifier = Modifier.fillMaxWidth()
//            )
//            Spacer(Modifier.height(16.dp))
//            Button(
//                onClick = { authViewModel.handleEvent(AuthEvent.Login(loginState.email, loginState.password)) { if (it) onLoginSuccess() } },
//                modifier = Modifier.fillMaxWidth(),
//                enabled = loginState.isValid,
//                colors = ButtonDefaults.buttonColors(containerColor = YellowPrimary)
//            ) { Text("Log In", color = BlackPrimary) }
//            Spacer(Modifier.height(8.dp))
//            Button(
//                onClick = onSignUpNavigate,
//                modifier = Modifier.fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(containerColor = Rose)
//            ) { Text("Sign Up", color = BlackPrimary) }
//            Spacer(Modifier.height(8.dp))
//            TextButton(onClick = { showForgotDialog = true }) {
//                Text("¿Has olvidado tu contraseña?", color = YellowLight)
//            }
//        }
//        if (showForgotDialog) ForgotPasswordDialog(
//            initialEmail = forgotEmail,
//            onEmailChange = { forgotEmail = it },
//            onSend = { authViewModel.handleEvent(AuthEvent.ForgotPassword(forgotEmail)) {}, showForgotDialog = false },
//            onDismiss = { showForgotDialog = false }
//        )
//    }
//}
//
//@Composable
//fun ForgotPasswordDialog(
//    initialEmail: String,
//    onEmailChange: (String) -> Unit,
//    onSend: () -> Unit,
//    onDismiss: () -> Unit
//) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Recuperar contraseña") },
//        text = {
//            OutlinedTextField(
//                value = initialEmail,
//                onValueChange = onEmailChange,
//                label = { Text("E-mail") },
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    textColor = Color.White,
//                    focusedBorderColor = YellowPrimary,
//                    unfocusedBorderColor = YellowLight,
//                    cursorColor = YellowPrimary,
//                    focusedLabelColor = YellowPrimary,
//                    unfocusedLabelColor = YellowLight
//                ),
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
//            )
//        },
//        confirmButton = { TextButton(onClick = onSend) { Text("Enviar") } },
//        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
//    )
//}
//
//// File: commonMain/kotlin/org/courselab/app/ui/screens/SignUpScreen.kt
//package org.courselab.app.ui.screens
//
//@Composable
//fun SignUpScreen(
//    authViewModel: AuthViewModel,
//    onSignUpComplete: (Boolean) -> Unit
//) {
//    val state by authViewModel.signUpState.collectAsState()
//    var showDialog by remember { mutableStateOf(false) }
//    var lastSuccess by remember { mutableStateOf(false) }
//
//    Column(
//        Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Top
//    ) {
//        Image(painterResource(Res.drawable.logo), contentDescription = "Logo", modifier = Modifier.size(100.dp))
//        Spacer(Modifier.height(16.dp))
//        listOf(
//            "Nombre" to state.nombre,
//            "Apellidos" to state.apellidos,
//            "E-mail" to state.email,
//            "Contraseña" to state.password,
//            "Fecha de nacimiento" to state.fechaNacimiento,
//            "Género" to state.genero
//        ).forEach { (label, value) ->
//            OutlinedTextField(
//                value = value,
//                onValueChange = { authViewModel.onSignUpInputChanged(label.lowercase(), it) },
//                label = { Text(label) },
//                visualTransformation = if (label == "Contraseña") PasswordVisualTransformation() else VisualTransformation.None,
//                keyboardOptions = if (label == "E-mail") KeyboardOptions(keyboardType = KeyboardType.Email) else KeyboardOptions.Default,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 4.dp)
//            )
//        }
//        Spacer(Modifier.height(16.dp))
//        Button(
//            onClick = { authViewModel.handleEvent(AuthEvent.SignUp(state)) { success -> lastSuccess = success; showDialog = true; onSignUpComplete(success) } },
//            enabled = state.isValid,
//            modifier = Modifier.fillMaxWidth(),
//            colors = ButtonDefaults.buttonColors(containerColor = YellowPrimary)
//        ) { Text("SIGN UP", color = BlackPrimary) }
//    }
//    if (showDialog) AlertDialog(
//        onDismissRequest = { showDialog = false },
//        title = { Text(if (lastSuccess) "Éxito" else "Error") },
//        text = { Text(if (lastSuccess) "Usuario creado correctamente" else "Error al crear usuario") },
//        confirmButton = { TextButton(onClick = { showDialog = false }) { Text("OK") } }
//    )
//}
//
//// File: commonMain/kotlin/org/courselab/app/viewmodel/AuthViewModel.kt
//package org.courselab.app.viewmodel
//
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//
//data class LoginFormState(val email: String = "", val password: String = "", val isValid: Boolean = false)
//
//data class SignUpFormState(
//    val nombre: String = "", val apellidos: String = "", val email: String = "", val password: String = "",
//    val fechaNacimiento: String = "", val genero: String = "", val isValid: Boolean = false
//)
//
//sealed class AuthEvent { data class Login(val email: String, val password: String): AuthEvent(); data class SignUp(val form: SignUpFormState): AuthEvent(); data class ForgotPassword(val email: String): AuthEvent() }
//
//class AuthViewModel {
//    private val _loginState = MutableStateFlow(LoginFormState())
//    val loginState: StateFlow<LoginFormState> = _loginState
//
//    private val _signUpState = MutableStateFlow(SignUpFormState())
//    val signUpState: StateFlow<SignUpFormState> = _signUpState
//
//    fun onLoginInputChanged(email: String, password: String) {
//        _loginState.value = LoginFormState(email, password, email.isNotBlank() && password.isNotBlank())
//    }
//    fun onSignUpInputChanged(field: String, value: String) {
//        val current = _signUpState.value
//        val updated = when(field) {
//            "nombre" -> current.copy(nombre = value)
//            "apellidos" -> current.copy(apellidos = value)
//            "e-mail" -> current.copy(email = value)
//            "contraseña" -> current.copy(password = value)
//            "fecha de nacimiento" -> current.copy(fechaNacimiento = value)
//            "género" -> current.copy(genero = value)
//            else -> current
//        }
//        _signUpState.value = updated.copy(isValid = updated.nombre.isNotBlank() && updated.apellidos.isNotBlank() && updated.email.isNotBlank() && updated.password.isNotBlank())
//    }
//    fun handleEvent(event: AuthEvent, onResult: (Boolean)->Unit) {
//        CoroutineScope(Dispatchers.Main).launch {
//            val success = true // TODO: call service
//            onResult(success)
//        }
//    }
//}
//
//// File: commonMain/kotlin/org/courselab/app/App.kt
//package org.courselab.app
//
//import org.courselab.app.ui.screens.WelcomeScreen
//import	org.courselab.app.ui.screens.SignUpScreen
//
//@Composable
//fun App() {
//    val authViewModel = remember { AuthViewModel() }
//    var currentScreen by remember { mutableStateOf("welcome") }
//
//    AppTheme {
//        when (currentScreen) {
//            "welcome" -> WelcomeScreen(
//                authViewModel = authViewModel,
//                onLoginSuccess = { /* TODO navigate to Home */ },
//                onSignUpNavigate = { currentScreen = "signup" }
//            )
//            "signup" -> SignUpScreen(
//                authViewModel = authViewModel,
//                onSignUpComplete = { success -> if (success) currentScreen = "welcome" }
//            )
//        }
//    }
//}
//
//// File: androidMain/kotlin/org/courselab/app/MainActivity.kt
//package org.courselab.app
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        enableEdgeToEdge()
//        super.onCreate(savedInstanceState)
//        installSplashScreen()
//        setContent {
//            App()
//        }
//    }
//}
