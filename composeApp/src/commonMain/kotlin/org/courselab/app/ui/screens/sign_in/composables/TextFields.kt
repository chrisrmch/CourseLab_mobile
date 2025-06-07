package org.courselab.app.ui.screens.sign_in.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.e_mail
import courselab.composeapp.generated.resources.email
import courselab.composeapp.generated.resources.password
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun GradientScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = colorScheme.background,
    contentColor: Color = colorScheme.onBackground,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = snackbarHost,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
        topBar = topBar,
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(padding)
                .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorScheme.surfaceBright,
                        colorScheme.surfaceContainerHigh
                    )
                )
            ),
        ) {
            content(padding)
        }
    }


}

data class FormField(
    val label: String,
    val onValueChanged: (String) -> Unit
)

@Composable
@Preview
fun FormScaffold(
    fields: List<FormField> = emptyList(),
    fieldValues: List<() -> String> = emptyList(),
    modifier: Modifier = Modifier,
    onDoneAction: (() -> Unit)? = null,
) {
    Column(modifier) {
        fields.forEachIndexed { index, (fieldValue, onValueChange) ->
            if (fieldValue.trim()
                    .lowercase() == "email"
                || fieldValue.trim()
                    .lowercase() == "e-mail"
            ) {
                BuildEmailTextField(
                    fields = fieldValues,
                    index = index,
                    label = fieldValue,
                    onTextEditing = onValueChange
                )
                Spacer(Modifier.height(9.dp))
            }
            if (fieldValue.trim().lowercase() == stringResource(Res.string.password)) {
                SecurePasswordTextField(
                    value = fieldValues.getOrNull(index)?.invoke() ?: "",
                    onValueChange = onValueChange,
                    myLabel = fieldValue,
                    onDoneAction = onDoneAction
                )
            } else if(fieldValue.trim().lowercase() != stringResource(Res.string.e_mail)
                && fieldValue.trim().lowercase() != stringResource(Res.string.email)
            ) {
                BuildTextField(
                    fields = fieldValues,
                    index = index,
                    label = fieldValue,
                    onTextEditing = onValueChange
                )
            }
            if (index == fields.lastIndex) {
                Spacer(Modifier.height(19.dp))
            }
        }
    }
}

@Composable
fun BuildEmailTextField(
    fields: List<() -> String>,
    label: String,
    index: Int,
    onTextEditing: (String) -> Unit,
) {
    OutlinedTextField(
        shape = RoundedCornerShape(20.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        maxLines = 1,
        modifier = Modifier.fillMaxWidth(),
        value = fields.getOrNull(index)?.invoke() ?: "",
        onValueChange = onTextEditing,
        label = { Text(label) },
        colors = textFieldColors()
    )
}

@Composable
fun BuildTextField(
    fields: List<() -> String>,
    label: String,
    index: Int,
    onTextEditing: (String) -> Unit,
) {
    OutlinedTextField(
        shape = RoundedCornerShape(20.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        maxLines = 1,
        modifier = Modifier.fillMaxWidth(),
        value = fields.getOrNull(index)?.invoke() ?: "",
        onValueChange = onTextEditing,
        label = { Text(label) },
        colors = textFieldColors()
    )
}



@Composable
fun SecurePasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    myLabel: String,
    modifier: Modifier = Modifier,
    onDoneAction: (() -> Unit)? = null,
) {
    val colorScheme = MaterialTheme.colorScheme
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= (value.length + 1)) {
                onValueChange(it)
            }
        },
        label = { Text(myLabel) },
        visualTransformation = PasswordVisualTransformation(mask = '\u2022'),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                onDoneAction?.invoke()
            }
        ),
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = textFieldColors(),
            singleLine = true,
        maxLines = 1
    )
}



@Composable
private fun textFieldColors() : TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = colorScheme.primary,
        unfocusedTextColor = colorScheme.outlineVariant,
        cursorColor = colorScheme.primary,
        unfocusedBorderColor = colorScheme.outlineVariant,
        focusedBorderColor = colorScheme.onBackground,
        unfocusedLabelColor = colorScheme.outlineVariant,
        focusedLabelColor = colorScheme.onBackground
    );
}