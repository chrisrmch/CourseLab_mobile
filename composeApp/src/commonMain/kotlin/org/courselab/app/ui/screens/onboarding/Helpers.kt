package org.courselab.app.ui.screens.onboarding


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun GenderSelector(
    selected: String?,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(vertical = 4.dp).fillMaxWidth()
    ) {
        Text(
            text = "Género",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Column(Modifier.selectableGroup()) {
            Row(
                Modifier.fillMaxWidth().height(56.dp).selectable(
                        selected = (selected == Sex.HOMBRE.name),
                        onClick = { onSelected(Sex.HOMBRE.name) },
                        role = Role.RadioButton
                    ).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    modifier = Modifier.semantics {
                        contentDescription = "Hombre"
                    },
                    selected = (selected == Sex.HOMBRE.name),
                    onClick = null // null recommended for accessibility with screenreaders
                )
                Text(
                    text = Sex.HOMBRE.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
        Row(
            Modifier.fillMaxWidth().height(56.dp).selectable(
                    selected = (selected == Sex.MUJER.name),
                    onClick = { onSelected(Sex.MUJER.name) },
                    role = Role.RadioButton
                ).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                modifier = Modifier.semantics {
                    contentDescription = "Mujer" //TODO("i18n Must do Internationalization")
                },
                selected = (selected == Sex.MUJER.name),
                onClick = null // null recommended for accessibility with screenreaders
            )
            Text(
                text = Sex.MUJER.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}


@Composable
fun PhotoPicker(
    currentPhotoUri: String?,
    onPickPhoto: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(100.dp).clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant).clickable { onPickPhoto() },
        contentAlignment = Alignment.Center
    ) {
        if (currentPhotoUri != null) {
            // En un módulo Android real haríamos algo como:
            // val painter = rememberAsyncImagePainter(currentPhotoUri)
            // Image(painter = painter, contentDescription = "Foto de perfil", modifier = Modifier.fillMaxSize().clip(CircleShape))
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Foto seleccionada",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Seleccionar foto de perfil",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
    Text(
        text = "Foto de perfil",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp)
    )
}


/**
 * TextField que permite varias líneas (ideal para biografía).
 * - value / onValueChange: texto actual y callback
 * - label: etiqueta a mostrar
 * - minLines: número mínimo de líneas
 */
@Composable
fun MultilineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    minLines: Int = 3,
    modifier: Modifier = Modifier,
) {
    val colors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        cursorColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedLabelColor = MaterialTheme.colorScheme.primary
    )
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        colors = colors,
        singleLine = false,
        minLines = minLines,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Default
        ),
        keyboardActions = KeyboardActions.Default
    )
}


/**
 * InterestsInput: el usuario puede escribir un interés en un TextField y presionar el ícono
 * de “Agregar” para incorporarlo a la lista. Cada interés se muestra como FilterChip, que se
 * puede eliminar.
 *
 * - interests: lista de intereses actuales
 * - onAddInterest: callback cuando se agrega un interés
 * - onRemoveInterest: callback cuando se elimina un interés
 */

@Composable
fun InterestsInput(
    interests: List<String>,
    onAddInterest: (String) -> Unit,
    onRemoveInterest: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentInput by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        Text(
            text = "Intereses (agrega uno a uno)",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = currentInput,
                onValueChange = { currentInput = it },
                modifier = Modifier.weight(1f),
                label = { Text("Nuevo interés") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                ),
                singleLine = true,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onAddInterest(currentInput)
                            currentInput = ""
                        }, enabled = currentInput.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add, contentDescription = "Agregar interés"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions.Default
            )
        }

        // Muestra los chips de intereses en una fila horizontal con scroll si excede ancho
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            interests.forEach { interest ->
                FilterChip(
                    selected = true, // siempre seleccionado porque es uno propio
                    onClick = { /* no toggle: es fijo */ },
                    label = { Text(interest) },
                    leadingIcon = null,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Eliminar $interest"
                        )
                    },
//                    onTrailingIconClick = { onRemoveInterest(interest) }
                )
            }
        }
    }
}
