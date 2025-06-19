package org.courselab.app.ui.screens.sign_in.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.cancel
import courselab.composeapp.generated.resources.email
import courselab.composeapp.generated.resources.recover_password
import courselab.composeapp.generated.resources.send
import org.courselab.app.ui.screens.onboarding.dto.Validator
import org.jetbrains.compose.resources.stringResource

@Composable
fun ForgotPasswordDialog(
    initialEmail: String = "",
    onEmailChange: (String) -> Unit = {},
    onSend: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        onDismissRequest = onDismiss,
        title = {
            Text(
                stringResource(Res.string.recover_password),
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = TextUnit(22f, MaterialTheme.typography.titleLarge.fontSize.type),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        },
        text = {
            FormScaffold(
                fields = listOf(FormField(stringResource(Res.string.email), onEmailChange)),
                fieldValues = listOf({ initialEmail }),
                canDoDoneAction = Validator.validateEmail(initialEmail),
                showLastSpace = false
            )
        },
        confirmButton = {
            OutlinedWelcomeButtons.Secondary(
                modifier = Modifier.width(130.dp),
                text = stringResource(Res.string.send),
                onClick = onSend,
                enabled = Validator.validateEmail(initialEmail)
            )
        },
        dismissButton = {
            OutlinedWelcomeButtons.Primary(
                modifier = Modifier.width(130.dp),
                text = stringResource(Res.string.cancel),
                onClick = onDismiss,
                enabled = true
            )
        })
}
