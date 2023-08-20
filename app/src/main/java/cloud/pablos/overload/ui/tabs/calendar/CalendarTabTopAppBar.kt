package cloud.pablos.overload.ui.tabs.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cloud.pablos.overload.R
import cloud.pablos.overload.data.item.ItemEvent
import cloud.pablos.overload.data.item.ItemState

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarTabTopAppBar(
    state: ItemState,
    onEvent: (ItemEvent) -> Unit,
) {
    val yearDialogState = remember { mutableStateOf(false) }

    Surface(
        tonalElevation = NavigationBarDefaults.Elevation,
        color = MaterialTheme.colorScheme.background,
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.calendar).replaceFirstChar { it.uppercase() },
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Normal,
                    ),
                )
            },
            actions = {
                Button(
                    onClick = { yearDialogState.value = true },
                    modifier = Modifier.padding(horizontal = 8.dp),
                ) {
                    Text(state.selectedYear.toString())
                }
                if (yearDialogState.value) {
                    CalendarTabYearDialog(onClose = { yearDialogState.value = false }, onEvent = onEvent)
                }
            },
        )
    }
}

/*@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun CalendarTabTopAppBarPreview() {
    com.pablos.overload.ui.tabs.ConfigurationsTab()
}
*/
