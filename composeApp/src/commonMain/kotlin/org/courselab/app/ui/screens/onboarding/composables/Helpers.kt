package org.courselab.app.ui.screens.onboarding.composables


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.sharp.DateRange
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.date_picker_title
import courselab.composeapp.generated.resources.female
import courselab.composeapp.generated.resources.icon_hombre
import courselab.composeapp.generated.resources.icon_mujer
import courselab.composeapp.generated.resources.male
import courselab.composeapp.generated.resources.photo_picker_label
import courselab.composeapp.generated.resources.photo_picker_photo_selected
import courselab.composeapp.generated.resources.photo_picker_select_profile_photo
import courselab.composeapp.generated.resources.select_dob
import courselab.composeapp.generated.resources.sex
import org.courselab.app.ui.screens.onboarding.viewModel.Sex
import org.jetbrains.compose.resources.painterResource
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
            color = colorScheme.onSurface
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
                    onClick = null
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
                    contentDescription = "Women" //TODO("i18n Must do Internationalization")
                },
                selected = (selected == Sex.MUJER.name),
                onClick = null
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
    modifier: Modifier = Modifier,
    currentPhoto: ImageBitmap?,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val shape = RoundedCornerShape(
            topStart = 25.dp,
            topEnd = 25.dp,
            bottomStart = 10.dp,
            bottomEnd = 10.dp
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .clip(shape)
                .background(colorScheme.surfaceVariant)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            if (currentPhoto != null) {
                Image(
                    bitmap = currentPhoto,
                    contentDescription = stringResource(
                        Res.string.photo_picker_photo_selected
                    ),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                val color = MaterialTheme.colorScheme.scrim

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .drawWithContent {
                            drawContent()
                            val startY = size.height * 0.65f
                            val scrimHeight = size.height * 0.35f

                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        color.copy(alpha = 0.75f)
                                    ),
                                    startY = startY,
                                    endY = startY + scrimHeight
                                ),
                                topLeft = Offset(0f, startY),
                                size = Size(size.width, scrimHeight)
                            )
                        }
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(
                            Res.string.photo_picker_select_profile_photo
                        ),
                        modifier = Modifier.size(48.dp).offset(x = 8.dp),
                        tint = colorScheme.onSurfaceVariant
                    )
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(
                            Res.string.photo_picker_select_profile_photo
                        ),
                        modifier = Modifier.size(38.dp).offset(x = (-8).dp),
                        tint = colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Text(
            text = stringResource(Res.string.photo_picker_label),
            style = MaterialTheme.typography.bodySmall,
            color = colorScheme.onSurfaceVariant,
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
        focusedTextColor = colorScheme.onSurface,
        unfocusedTextColor = colorScheme.onSurfaceVariant,
        cursorColor = colorScheme.primary,
        unfocusedBorderColor = colorScheme.outlineVariant,
        focusedBorderColor = colorScheme.primary,
        unfocusedLabelColor = colorScheme.onSurfaceVariant,
        focusedLabelColor = colorScheme.primary
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

@Composable
fun FechaConOverlay(
    modifier: Modifier,
    fecha: String,
    onClickAbrirPicker: () -> Unit,
    borderRadius: Dp = 25.dp,
) {
    val shape = RoundedCornerShape(borderRadius)
    val focusManager = LocalFocusManager.current
    val wasFocused = remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier.onFocusChanged { state ->
            if (state.isFocused && !wasFocused.value) {
                onClickAbrirPicker()
                focusManager.clearFocus(force = true)
            }
            wasFocused.value = state.isFocused
        }.fillMaxWidth().clip(shape),
        shape = shape,
        value = fecha,
        onValueChange = {},
        readOnly = true,
        label = { Text(stringResource(Res.string.date_picker_title)) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Sharp.DateRange,
                contentDescription = stringResource(Res.string.select_dob)
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = colorScheme.onSurface,
            unfocusedTextColor = colorScheme.onSurfaceVariant,
            cursorColor = colorScheme.primary,
            unfocusedBorderColor = colorScheme.outlineVariant,
            focusedBorderColor = colorScheme.primary,
            unfocusedLabelColor = colorScheme.onSurfaceVariant,
            focusedLabelColor = colorScheme.primary
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderCard(
    modifier: Modifier,
    selectedGender: Sex?,
    cardGender: Sex,
    onGenderSelected: (Sex) -> Unit
) {
    val shape = RoundedCornerShape(25.dp)
    val isSelected = cardGender == selectedGender
    OutlinedCard(
        onClick = { onGenderSelected(cardGender) },
        modifier = modifier.fillMaxWidth().clip(shape),
        shape = shape,
        colors = cardColors(isSelected),
        border = BorderStroke(1.dp, getGenderCardColor(isSelected))
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().clip(shape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = when (cardGender) {
                    Sex.HOMBRE -> painterResource(Res.drawable.icon_hombre)
                    Sex.MUJER -> painterResource(Res.drawable.icon_mujer)
                },
                contentDescription = stringResource(Res.string.sex),
                modifier = Modifier
                    .padding(24.dp),
                tint = getGenderCardColor(isSelected = isSelected)
            )
        }
    }
}

@Composable
private fun cardColors(isSelected: Boolean) = if (isSelected) {
    CardDefaults.cardColors(
        containerColor = colorScheme.surfaceVariant,
        contentColor = colorScheme.onSurfaceVariant
    )
} else {
    CardDefaults.cardColors(
        containerColor = colorScheme.surface,
        contentColor = colorScheme.onSurface
    )
}

@Composable
private fun getGenderCardColor(isSelected: Boolean) =
    if (isSelected)
        colorScheme.tertiary
    else colorScheme.secondaryContainer.copy(alpha = 0.5f)
