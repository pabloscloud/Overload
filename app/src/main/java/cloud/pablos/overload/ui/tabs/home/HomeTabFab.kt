package cloud.pablos.overload.ui.tabs.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
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

    val isOngoing = itemsForToday.isNotEmpty() && itemsForToday.last().ongoing
    val icon = if (isOngoing) Icons.Default.Stop else Icons.Default.PlayArrow
    val buttonText = if (isOngoing) stringResource(id = R.string.stop) else stringResource(id = R.string.start)

    FloatingActionButton(
        onClick = {
            startOrStopPause(state, onEvent)
        },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = Modifier.animateContentSize(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = buttonText,
                modifier = Modifier.padding(8.dp),
            )
            TextView(
                text = buttonText,
                modifier = Modifier.padding(end = 8.dp),
            )
        }
    }
}
