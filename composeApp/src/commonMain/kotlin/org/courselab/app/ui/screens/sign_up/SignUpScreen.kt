package org.courselab.app.ui.screens.sign_up


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.accept
import courselab.composeapp.generated.resources.already_have_account
import courselab.composeapp.generated.resources.email
import courselab.composeapp.generated.resources.error_while_creating_user
import courselab.composeapp.generated.resources.last_step_confirm_account_HEAD
import courselab.composeapp.generated.resources.logo
import courselab.composeapp.generated.resources.password
import courselab.composeapp.generated.resources.sign_up
import courselab.composeapp.generated.resources.sign_up_on_proces
import courselab.composeapp.generated.resources.sign_up_succesfully_done
import courselab.composeapp.generated.resources.something_went_wrong
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.courselab.app.org.courselab.app.LocalNavController
import org.courselab.app.org.courselab.app.ui.screens.sign_up.dto.SignUpRequestDTO
import org.courselab.app.ui.screens.log_in.composables.FormField
import org.courselab.app.ui.screens.log_in.composables.FormScaffold
import org.courselab.app.ui.screens.log_in.composables.GradientScaffold
import org.courselab.app.ui.screens.log_in.composables.OutlinedWelcomeButtons
import org.courselab.app.ui.screens.log_in.composables.ThemeToggle
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject


@Preview
@Composable
fun SignUpScreen(
    logo: Painter?,
) {
    val signUpViewModel = koinInject<SignUpViewModel>()
    val navController = LocalNavController.current

//VARIABLES CONTROL ESTADO PANTALLA
    val formState by signUpViewModel.signUpState.collectAsState()
    var isDialogVisible: Boolean by remember { mutableStateOf(false) }
    var lastSuccess by remember { mutableStateOf(false) }
    val isLoading by signUpViewModel.isLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        signUpViewModel.snackbarMsg.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    GradientScaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { it ->
        Column(
            modifier = Modifier.fillMaxSize().padding(it).padding(16.dp).offset(y = (-26.9).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            logo?.let {
                Image(
                    it,
                    contentDescription = stringResource(Res.string.logo),
                    modifier = Modifier.size(180.dp).offset(y = (-50).dp)
                        .clip(RoundedCornerShape(15.dp))
                )
            }
            FormScaffold(
                fields = listOf(
                    FormField(stringResource(Res.string.email), { signUpViewModel.onSignUpInputChanged("E-mail", it) } ),
                    FormField(stringResource(Res.string.password),  { signUpViewModel.onSignUpInputChanged("Password", it) } )
                ),
                fieldValues = listOf(
                    { formState.email },
                    { formState.password }
                ), onDoneAction = {
                    signUpViewModel.onSignUpFormSubmitted(
                        SignUpRequestDTO(
                            email = formState.email,
                            password = formState.password
                        )
                    ) { success -> lastSuccess = success; isDialogVisible = true
                        signUpViewModel.scope.launch {
                            delay(3000)
                        }
                        navController?.popBackStack()
                    }
                }
            )

            OutlinedWelcomeButtons.Primary(
                onClick = {
                    signUpViewModel.onSignUpFormSubmitted(
                        SignUpRequestDTO(
                            email = formState.email,
                            password = formState.password
                        )
                    ) { success ->
                        lastSuccess = success; isDialogVisible = true
                        signUpViewModel.scope.launch {
                            delay(3000)
                            navController?.popBackStack()
                        }
                    }
                },
                enabled = formState.isValid,
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(Res.string.sign_up_on_proces), color = MaterialTheme.colorScheme.onSecondary)
                } else {
                    Text(
                        stringResource(Res.string.sign_up),
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            OutlinedWelcomeButtons.Secondary(
                text = stringResource(Res.string.already_have_account),
                onClick = { navController?.popBackStack()},
            )

            ThemeToggle()
        }
    }

    if (isDialogVisible) {
        AlertDialog(
            onDismissRequest = { isDialogVisible = false },
            title = {
                Text(
                    text = if (lastSuccess) stringResource(Res.string.last_step_confirm_account_HEAD) else stringResource(Res.string.something_went_wrong),
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (lastSuccess) Color.Black else Color.Red
                )
            },
            text = {
                Text(
                    text = if (lastSuccess) stringResource(Res.string.sign_up_succesfully_done) else stringResource(Res.string.error_while_creating_user),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = { isDialogVisible = false }) {
                    Text(
                        stringResource(Res.string.accept),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        )
    }
}