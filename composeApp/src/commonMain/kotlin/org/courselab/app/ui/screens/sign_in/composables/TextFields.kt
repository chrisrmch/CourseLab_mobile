package org.courselab.app.ui.screens.sign_in.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.ktor.util.toLowerCasePreservingASCIIRules
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun GradientScaffold(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = colorScheme.background,
        contentColor = colorScheme.onBackground
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorScheme.background,
                        colorScheme.onSurfaceVariant
                    )
                )
            ).padding(padding)
        ) {
            content(padding)
        }
    }
}


@Composable
@Preview
fun FormScaffold(
    fields: List<Pair<String, (String) -> Unit>> = emptyList(),
    fieldValues: List<() -> String> = emptyList(),
    modifier: Modifier = Modifier,
    onDoneAction: () -> Unit,
) {
    Column(modifier) {
        fields.forEachIndexed { index, (fieldValue, onValueChange) ->
            if (fieldValue.trim().toLowerCasePreservingASCIIRules() == "email" || fieldValue.trim()
                    .toLowerCasePreservingASCIIRules() == "e-mail"
            ) {
                BuildTextField(
                    fields = fieldValues,
                    index = index,
                    label = fieldValue,
                    onTextEditing = onValueChange
                )
                Spacer(Modifier.height(9.dp))
            }
            if (fieldValue.trim()
                    .toLowerCasePreservingASCIIRules() == "password"
            ) {
                SecurePasswordTextField(
                    value = fieldValues.getOrNull(index)?.invoke() ?: "",
                    onValueChange = onValueChange,
                    myLabel = fieldValue,
                    onDoneAction = onDoneAction
                )
            }
            if(index == fields.lastIndex) {
                Spacer(Modifier.height(19.dp))
            }
        }
    }
}

@Composable
fun BuildTextField(
    fields: List<() -> String>,
    label : String,
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
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = colorScheme.onPrimary,
            unfocusedTextColor = colorScheme.onSurfaceVariant,
            focusedBorderColor = colorScheme.onBackground,
            unfocusedBorderColor = colorScheme.onSurfaceVariant,
            cursorColor = colorScheme.inverseSurface,
            focusedLabelColor = colorScheme.onPrimary,
            unfocusedLabelColor = colorScheme.onSurfaceVariant
        ),
    )
}