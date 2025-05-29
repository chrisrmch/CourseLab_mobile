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
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.onSurfaceVariant
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
    onDoneAction: () -> Unit = {},
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(modifier) {
        fields.forEachIndexed { index, (fieldValue, onValueChange) ->
            if (index > 1 && index < fields.size - 1) {
                Spacer(Modifier.height(8.dp))
            }
            if (fieldValue.trim().toLowerCasePreservingASCIIRules() == "email" || fieldValue.trim()
                    .toLowerCasePreservingASCIIRules() == "e-mail"
            ) {
                BuildTextField(
                    fieldValues = fieldValues,
                    index = index,
                    onValueChange = onValueChange
                )
                Spacer(Modifier.height(9.dp))
            }
            if (fieldValue.trim()
                    .toLowerCasePreservingASCIIRules() == "contraseña" && index == fields.size - 1
            ) {
                SecurePasswordTextField(
                    value = fieldValues.getOrNull(index)?.invoke() ?: "",
                    onValueChange = onValueChange,
                    label = fieldValue,
                    onDoneAction = onDoneAction
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun BuildTextField(
    fieldValues: List<() -> String>,
    index: Int,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        shape = RoundedCornerShape(20.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        maxLines = 1,
        modifier = Modifier.fillMaxWidth(),
        value = fieldValues.getOrNull(index)?.invoke() ?: "",
        onValueChange = onValueChange,
        label = { Text(fieldValues.getOrNull(index)?.invoke() ?: "") },
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