package cloud.pablos.overload.ui.tabs.configurations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cloud.pablos.overload.R
import cloud.pablos.overload.ui.views.TextView

@Composable
fun ConfigurationsTabPauseGoalDialog(onClose: () -> Unit) {
    var hours by remember { mutableStateOf<Int?>(null) }
    var minutes by remember { mutableStateOf<Int?>(null) }

    val hoursValidator = (hours ?: 0) < 24
    val minValidator = (minutes ?: 0) < 60

    val hoursFocusRequest = remember { FocusRequester() }
    val minFocusRequest = remember { FocusRequester() }

    val context = LocalContext.current
    val sharedPreferences = remember { OlSharedPreferences(context) }

    LaunchedEffect(Unit) {
        hoursFocusRequest.requestFocus()
    }

    AlertDialog(
        onDismissRequest = { onClose() },
        icon = {
            Icon(
                imageVector = Icons.Rounded.Timer,
                contentDescription = stringResource(id = R.string.pick_pause_goal),
                tint = MaterialTheme.colorScheme.primary,
            )
        },
        title = {
            TextView(
                text = stringResource(id = R.string.pick_pause_goal),
                fontWeight = FontWeight.Bold,
                align = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    value = hours?.toString() ?: "",
                    onValueChange = {
                        hours = it.toIntOrNull()
                    },
                    singleLine = true,
                    suffix = { Text(text = "hours") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        if (hoursValidator) {
                            minFocusRequest.requestFocus()
                        }
                    }),
                    placeholder = { Text(text = "0") },
                    isError = !hoursValidator,
                    modifier = Modifier.focusRequester(hoursFocusRequest),
                )

                TextField(
                    value = minutes?.toString() ?: "",
                    onValueChange = {
                        minutes = it.toIntOrNull()
                    },
                    singleLine = true,
                    suffix = { Text(text = "minutes") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        saveAndClose(
                            onClose = onClose,
                            sharedPreferences = sharedPreferences,
                            hours = hours,
                            minutes = minutes,
                            valid = hoursValidator && minValidator,
                        )
                    }),
                    placeholder = { Text(text = "0") },
                    isError = !minValidator,
                    modifier = Modifier.focusRequester(minFocusRequest),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    saveAndClose(
                        onClose = onClose,
                        sharedPreferences = sharedPreferences,
                        hours = hours,
                        minutes = minutes,
                        valid = hoursValidator && minValidator,
                    )
                },
            ) {
                TextView(stringResource(id = R.string.save))
            }
        },
        dismissButton = {
            Button(
                onClick = { onClose() },
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            ) {
                Text(text = "Cancel")
            }
        },
        modifier = Modifier.padding(16.dp),
    )
}

private fun saveAndClose(
    onClose: () -> Unit,
    sharedPreferences: OlSharedPreferences,
    hours: Int?,
    minutes: Int?,
    valid: Boolean,
) {
    if (valid) {
        val hoursInMin = (hours?.times(60) ?: 0)
        val minutesInMin = (minutes ?: 0)

        val goal = (hoursInMin + minutesInMin) * 60 * 1000

        if (goal > 0) {
            sharedPreferences.savePauseGoal(goal)

            onClose()
        }
    }
}

@Preview
@Composable
fun ConfigurationsTabPauseGoalPreview() {
    ConfigurationsTabPauseGoalDialog {}
}
