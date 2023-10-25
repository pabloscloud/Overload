package cloud.pablos.overload.ui.tabs.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cloud.pablos.overload.R
import cloud.pablos.overload.data.item.ItemEvent
import cloud.pablos.overload.data.item.ItemState
import cloud.pablos.overload.data.item.startOrStopPause
import cloud.pablos.overload.ui.views.TextView
import cloud.pablos.overload.ui.views.extractDate
import cloud.pablos.overload.ui.views.parseToLocalDateTime
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun HomeTabFab(
    state: ItemState,
    onEvent: (ItemEvent) -> Unit,
) {
    val date = LocalDate.now()

    val itemsForToday = state.items.filter { item ->
        val startTime = parseToLocalDateTime(item.startTime)
        extractDate(startTime) == date
    }

    FloatingActionButton(
        onClick = {
            startOrStopPause(state, onEvent)
        },
        modifier = Modifier
            .padding(10.dp),
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    ) {
        val isOngoing = itemsForToday.isNotEmpty() && itemsForToday.last().ongoing
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp),
        ) {
            Icon(
                imageVector =
                if (isOngoing) {
                    Icons.Default.Stop
                } else {
                    Icons.Default.PlayArrow
                },
                contentDescription = stringResource(id = R.string.edit),
                modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp),
            )
            TextView(
                text =
                if (isOngoing) {
                    stringResource(id = R.string.stop)
                } else {
                    stringResource(id = R.string.start)
                },
                modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 0.dp),
            )
        }
    }
}

/*@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun HomeTabPreview() {
    com.pablos.overload.ui.tabs.HomeTab()
}
*/
