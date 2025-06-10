package org.courselab.app.ui.screens.onboarding


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.female
import courselab.composeapp.generated.resources.male
import courselab.composeapp.generated.resources.photo_picker_label
import courselab.composeapp.generated.resources.photo_picker_photo_selected
import courselab.composeapp.generated.resources.photo_picker_select_profile_photo
import courselab.composeapp.generated.resources.sex
import org.jetbrains.compose.resources.stringResource

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
            text = stringResource(Res.string.sex),
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
                        contentDescription = ""
                    },
                    selected = (selected == Sex.HOMBRE.name),
                    onClick = null // null recommended for accessibility with screenreaders
                )
                Text(
                    text = stringResource(Res.string.male),
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
                text = stringResource(Res.string.female),
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
    Column(
        modifier = Modifier.clipToBounds(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            modifier = modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            onClick = onPickPhoto
        ) {
            if (currentPhotoUri != null) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(Res.string.photo_picker_photo_selected),
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(Res.string.photo_picker_select_profile_photo),
                        modifier = Modifier.size(48.dp).offset(x = 8.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(Res.string.photo_picker_select_profile_photo),
                        modifier = Modifier.size(38.dp).offset(x = (-8).dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        Text(
            text = stringResource(Res.string.photo_picker_label),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}


@Composable
fun MultilineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    minLines: Int = 2,
    maxLines: Int,
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
        maxLines = maxLines,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Default
        ),
        keyboardActions = KeyboardActions.Default
    )
}