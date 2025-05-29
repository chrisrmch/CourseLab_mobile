package org.courselab.app.ui.screens.sign_in.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import io.ktor.websocket.Frame.Text


@Composable
fun SecurePasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    onDoneAction: () -> Unit,
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
        label = { Text(label) },
        visualTransformation = PasswordVisualTransformation(mask = '\u2022'),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                onDoneAction()
            }
        ),
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = colorScheme.onPrimary,
            unfocusedTextColor = colorScheme.onSurfaceVariant,
            focusedBorderColor = colorScheme.onBackground,
            unfocusedBorderColor = colorScheme.onSurfaceVariant,
            cursorColor = colorScheme.inverseSurface,
            focusedLabelColor = colorScheme.onPrimary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        singleLine = true,
        maxLines = 1
    )
}

