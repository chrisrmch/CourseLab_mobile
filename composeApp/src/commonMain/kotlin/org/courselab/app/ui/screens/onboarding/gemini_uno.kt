@file:OptIn(ExperimentalMaterial3Api::class)
package org.courselab.app.ui.screens.onboarding



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import org.courselab.app.ui.screens.sign_in.composables.GradientScaffold
import org.courselab.app.ui.screens.sign_in.composables.OutlinedWelcomeButtons
import org.courselab.app.viewmodel.BaseViewModel
import org.koin.compose.koinInject

// --- Estados para los campos del Onboarding ---
data class OnboardingState(
    // Pantalla 1
    val nombre: String = "",
    val apellidos: String = "",
    val fechaNacimiento: LocalDate? = null,
    val genero: String = "", // Podría ser un enum o una constante

    // Pantalla 2
    val fotoPerfilUri: String = "", // URI o path a la imagen
    val biografia: String = "",
    val enlaceWeb: String = "",
    val ubicacion: String = "",
    val intereses: List<String> = emptyList(),

    val currentStep: Int = 1, // Para controlar la pantalla actual del onboarding
    val totalSteps: Int = 2
)

class OnboardingViewModel : BaseViewModel() {
    private val _onboardingState = MutableStateFlow(OnboardingState())
    val onboardingState: StateFlow<OnboardingState>  = _onboardingState

    // --- Funciones para actualizar el estado ---

    fun onNombreChanged(nombre: String) {
        _onboardingState.value = _onboardingState.value.copy(nombre = nombre)
    }

    fun onApellidosChanged(apellidos: String) {
        _onboardingState.value = _onboardingState.value.copy(apellidos = apellidos)
    }

    fun onFechaNacimientoChanged(fecha: LocalDate?) {
        _onboardingState.value = _onboardingState.value.copy(fechaNacimiento = fecha)
    }

    fun onGeneroChanged(genero: String) {
        _onboardingState.value = _onboardingState.value.copy(genero = genero)
    }

    fun onFotoPerfilChanged(uri: String) {
        _onboardingState.value = _onboardingState.value.copy(fotoPerfilUri = uri)
    }

    fun onBiografiaChanged(bio: String) {
        _onboardingState.value = _onboardingState.value.copy(biografia = bio)
    }

    fun onEnlaceWebChanged(enlace: String) {
        _onboardingState.value = _onboardingState.value.copy(enlaceWeb = enlace)
    }

    fun onUbicacionChanged(ubicacion: String) {
        _onboardingState.value = _onboardingState.value.copy(ubicacion = ubicacion)
    }

    fun onInteresesChanged(interesesTexto: String) {
        // Simple split por comas, se podría mejorar con chips
        val listaIntereses = interesesTexto.split(",").map { it.trim() }.filter { it.isNotBlank() }
        _onboardingState.value = _onboardingState.value.copy(intereses = listaIntereses)
    }

    fun navigateToNextStep() {
        if (_onboardingState.value.currentStep < _onboardingState.value.totalSteps) {
            _onboardingState.value = _onboardingState.value.copy(currentStep = _onboardingState.value.currentStep + 1)
        }
    }

    fun navigateToPreviousStep() {
        if (_onboardingState.value.currentStep > 1) {
            _onboardingState.value = _onboardingState.value.copy(currentStep = _onboardingState.value.currentStep - 1)
        }
    }

    fun submitOnboardingData() {
        val data = _onboardingState.value
        println("Datos del Onboarding para enviar: $data")
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen_gemini(
    onOnboardingComplete: () -> Unit
) {
    val viewModel: OnboardingViewModel = koinInject()
    val state by viewModel.onboardingState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    GradientScaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Completa tu Perfil (${state.currentStep}/${state.totalSteps})") },
                navigationIcon = {
                    if (state.currentStep > 1) {
                        IconButton(onClick = { viewModel.navigateToPreviousStep() }) {
                            Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Atrás")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), // Similar a tu Login
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Para contenido largo
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (state.currentStep) {
                    1 -> OnboardingStep1_gemini(
                        nombre = state.nombre,
                        onNombreChanged = viewModel::onNombreChanged,
                        apellidos = state.apellidos,
                        onApellidosChanged = viewModel::onApellidosChanged,
                        fechaNacimiento = state.fechaNacimiento,
                        onFechaNacimientoChanged = viewModel::onFechaNacimientoChanged,
                        genero = state.genero,
                        onGeneroChanged = viewModel::onGeneroChanged
                    )

                    2 -> OnboardingStep2_gemini(
                        fotoPerfilUri = state.fotoPerfilUri,
                        onFotoPerfilChanged = viewModel::onFotoPerfilChanged, // Placeholder
                        biografia = state.biografia,
                        onBiografiaChanged = viewModel::onBiografiaChanged,
                        enlaceWeb = state.enlaceWeb,
                        onEnlaceWebChanged = viewModel::onEnlaceWebChanged,
                        ubicacion = state.ubicacion,
                        onUbicacionChanged = viewModel::onUbicacionChanged,
                        intereses = state.intereses.joinToString(", "),
                        onInteresesChanged = viewModel::onInteresesChanged
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (state.currentStep > 1) Arrangement.SpaceBetween else Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.currentStep > 1) {
                    OutlinedWelcomeButtons.Secondary( // Reutilizamos tu botón secundario
                        text = "Anterior",
                        onClick = { viewModel.navigateToPreviousStep() },
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    )
                }
                OutlinedWelcomeButtons.Primary( // Reutilizamos tu botón primario
                    text = if (state.currentStep < state.totalSteps) "Siguiente" else "Finalizar",
                    onClick = {
                        if (state.currentStep < state.totalSteps) {
                            viewModel.navigateToNextStep()
                        } else {
                            viewModel.submitOnboardingData()
                            onOnboardingComplete() // Navegar fuera del onboarding
                        }
                    },
                    modifier = Modifier.weight(1f).padding(start = if (state.currentStep > 1) 8.dp else 0.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingStep1_gemini(
    nombre: String,
    onNombreChanged: (String) -> Unit,
    apellidos: String,
    onApellidosChanged: (String) -> Unit,
    fechaNacimiento: kotlinx.datetime.LocalDate?,
    onFechaNacimientoChanged: (kotlinx.datetime.LocalDate?) -> Unit,
    genero: String,
    onGeneroChanged: (String) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                // Permitir seleccionar fechas hasta el día de hoy
                return utcTimeMillis <= Clock.System.todayIn(TimeZone.currentSystemDefault()).atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    datePickerState.selectedDateMillis?.let { millis ->
                        onFechaNacimientoChanged(
                            Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.UTC).date
                        )
                    }
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            "Información Personal",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = onNombreChanged,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            colors = textFieldColors()
        )

        OutlinedTextField(
            value = apellidos,
            onValueChange = onApellidosChanged,
            label = { Text("Apellidos") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            colors = textFieldColors()
        )

        OutlinedTextField(
            value = fechaNacimiento?.toString() ?: "", // Formatear como desees
            onValueChange = { /* No editable directamente, se usa el picker */ },
            label = { Text("Fecha de Nacimiento") },
            modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Filled.Done, "Seleccionar fecha")
                }
            },
            readOnly = true,
            colors = textFieldColors(),
            enabled = false // Para que el click funcione en todo el campo
        )

        GenderSelector_gemini(
            selectedGender = genero,
            onGenderSelected = onGeneroChanged
        )
    }
}

@Composable
fun GenderSelector_gemini(
    selectedGender: String,
    onGenderSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val genders = listOf("Hombre", "Mujer", "Otro") // Opciones de género

    Column(modifier = modifier) {
        Text("Género", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            genders.forEach { gender ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = (selectedGender == gender),
                        onClick = { onGenderSelected(gender) },
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        text = gender,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.clickable { onGenderSelected(gender) }.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun OnboardingStep2_gemini(
    fotoPerfilUri: String,
    onFotoPerfilChanged: (String) -> Unit,
    biografia: String,
    onBiografiaChanged: (String) -> Unit,
    enlaceWeb: String,
    onEnlaceWebChanged: (String) -> Unit,
    ubicacion: String,
    onUbicacionChanged: (String) -> Unit,
    intereses: String, // Se maneja como String separado por comas para el TextField
    onInteresesChanged: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            "Detalles del Perfil",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        ImagePickerPlaceholder(
            imageUri = fotoPerfilUri,
            onImageClick = {
                // Aquí iría la lógica para abrir el selector de imágenes de la plataforma
                // Por ahora, simulamos un cambio con un placeholder
                onFotoPerfilChanged("nueva_uri_simulada.jpg")
                println("Selector de imagen presionado (simulado)")
            }
        )

        OutlinedTextField(
            value = biografia,
            onValueChange = onBiografiaChanged,
            label = { Text("Biografía") },
            modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp), // Multilínea
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text
            ),
            maxLines = 5,
            colors = textFieldColors()
        )

        OutlinedTextField(
            value = enlaceWeb,
            onValueChange = onEnlaceWebChanged,
            label = { Text("Enlace Web (opcional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Next
            ),
            colors = textFieldColors()
        )

        OutlinedTextField(
            value = ubicacion,
            onValueChange = onUbicacionChanged,
            label = { Text("Ubicación (opcional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            colors = textFieldColors()
        )

        OutlinedTextField(
            value = intereses,
            onValueChange = onInteresesChanged,
            label = { Text("Intereses (separados por coma)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done
            ),
            colors = textFieldColors()
        )
    }
}

@Composable
fun ImagePickerPlaceholder(
    imageUri: String,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholderIcon: ImageVector = Icons.Filled.Person
) {
    Box(
        modifier = modifier
            .size(120.dp)
            .clip(MaterialTheme.shapes.medium) // O CircleShape
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onImageClick() },
        contentAlignment = Alignment.Center
    ) {
        if (imageUri.isBlank()) { // Si no hay imagen, muestra el placeholder
            Icon(
                imageVector = placeholderIcon,
                contentDescription = "Seleccionar foto de perfil",
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            // Aquí cargarías la imagen real si la URI es válida y tienes un cargador de imágenes
            // Por ejemplo, usando Coil, Kamel, etc.
            // Image(painter = rememberImagePainter(imageUri), contentDescription = "Foto de perfil")
            Text("Imagen: $imageUri", style = MaterialTheme.typography.bodySmall) // Placeholder de texto
        }
    }
}