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
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
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
import courselab.composeapp.generated.resources.invalid_email
import courselab.composeapp.generated.resources.invalid_email_no_spaces_allowed
import courselab.composeapp.generated.resources.password
import org.courselab.app.ui.screens.onboarding.dto.Validator
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun GradientScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.EndOverlay,
    containerColor: Color = colorScheme.surfaceBright,
    contentColor: Color = colorScheme.onSurface,
    contentWindowInsets: WindowInsets = WindowInsets.waterfall,
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
                .windowInsetsPadding(contentWindowInsets)
                .background(
                    brush = GradientScaffoldBrush()
                ),
        ) {
            content(padding)
        }
    }
}

@Composable
fun GradientScaffoldBrush(): Brush {
    return Brush.verticalGradient(
        colors = listOf(
            colorScheme.surfaceContainerHigh,
            colorScheme.surfaceBright,
        )
    )
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
    canDoDoneAction: Boolean = true,
    showLastSpace: Boolean = true
) {
    val focusRequester = remember { FocusRequester() }

    Column(modifier) {
        fields.forEachIndexed { index, (label, onValueChange) ->
            if (isEmail(label)) {
                val emailTextFieldTouched = remember { mutableStateOf(false) }

                BuildEmailTextField(
                    text = fieldValues[index].invoke(),
                    label = label,
                    onTextEditing = onValueChange,
                    focusRequester = focusRequester,
                    emailTextFieldTouched = emailTextFieldTouched.value,
                    onEmailTextFieldFocusChange = { emailTextFieldTouched.value = it },
                )
                Spacer(Modifier.height(9.dp))
            } else if (isPassword(label)) {
                SecurePasswordTextField(
                    value = fieldValues[index].invoke(),
                    onValueChange = onValueChange,
                    myLabel = label,
                    onDoneAction = onDoneAction,
                    canDoDoneAction = canDoDoneAction,
                    focusRequester = focusRequester
                )
            } else {
                BuildTextField(
                    text = fieldValues[index].invoke(),
                    label = label,
                    onTextEditing = onValueChange
                )
            }
            if (index == fields.lastIndex && showLastSpace) {
                Spacer(Modifier.height(19.dp))
            }
        }
    }
}

@Composable
private fun isPassword(label: String) =
    label.lowercase() == stringResource(Res.string.password).lowercase()

@Composable
private fun isEmail(label: String) =
    (label.lowercase() == stringResource(Res.string.email).lowercase()
            || label.lowercase() == stringResource(Res.string.e_mail).lowercase())

@Composable
fun BuildEmailTextField(
    text: String,
    label: String,
    onTextEditing: (String) -> Unit,
    focusRequester: FocusRequester,
    onEmailTextFieldFocusChange: (Boolean) -> Unit,
    emailTextFieldTouched: Boolean
) {

    OutlinedTextField(
        shape = RoundedCornerShape(20.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                onEmailTextFieldFocusChange(true)
                focusRequester.requestFocus()
            }
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth().onFocusChanged { focusState ->
            if (focusState.isFocused) {
                onEmailTextFieldFocusChange(true)
            } else {
                onEmailTextFieldFocusChange(false)
            }
        },
        isError = !Validator.validateEmail(text) && text.isNotEmpty() && !emailTextFieldTouched,
        supportingText = {
            when {
                !text.contains("@") && text.isNotBlank() && emailTextFieldTouched -> Text(
                    "user@example.com",
                    color = colorScheme.outline.copy(alpha = 0.9f)
                )

                !Validator.validateEmail(text) && text.isNotEmpty() && !emailTextFieldTouched -> Text(
                    stringResource(Res.string.invalid_email, "user@example.com"),
                    color = colorScheme.errorContainer
                )

                text.contains(" ") -> Text(
                    stringResource(Res.string.invalid_email_no_spaces_allowed),
                    color = colorScheme.error
                )
            }
        },
        value = text,
        onValueChange = onTextEditing,
        label = { Text(label) },
        colors = textFieldColors()
    )
}

@Composable
fun BuildTextField(
    text: String,
    label: String,
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
        value = text,
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
    canDoDoneAction: Boolean = true,
    focusRequester: FocusRequester,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = value,
        onValueChange = { it: String ->
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
                if (canDoDoneAction) {
                    keyboardController?.hide()
                    onDoneAction?.invoke()
                } else {
                    focusRequester.requestFocus()
                }
            }
        ),
        modifier = modifier.fillMaxWidth().focusRequester(focusRequester),
        shape = RoundedCornerShape(20.dp),
        colors = textFieldColors(),
        singleLine = true,
        maxLines = 1
    )
}


@Composable
private fun textFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = colorScheme.primary,
        unfocusedTextColor = colorScheme.primary,
        cursorColor = colorScheme.primary,
        unfocusedBorderColor = colorScheme.outlineVariant,
        focusedBorderColor = colorScheme.onBackground,
        unfocusedLabelColor = colorScheme.outlineVariant,
        focusedLabelColor = colorScheme.onBackground
    );
}