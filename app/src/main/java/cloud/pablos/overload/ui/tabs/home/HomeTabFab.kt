package cloud.pablos.overload.ui.tabs.home

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cloud.pablos.overload.R
import cloud.pablos.overload.data.item.ItemEvent
import cloud.pablos.overload.data.item.ItemState
import cloud.pablos.overload.ui.utils.ShortcutUtil
import cloud.pablos.overload.ui.views.TextView
import cloud.pablos.overload.ui.views.extractDate
import cloud.pablos.overload.ui.views.parseToLocalDateTime
import java.time.LocalDate
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
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

    val context = LocalContext.current

    FloatingActionButton(
        onClick = {
            startOrStopPause(state, onEvent, context)
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

fun startOrStopPause(state: ItemState, onEvent: (ItemEvent) -> Unit, context: Context) {
    val date = LocalDate.now()

    val itemsForToday = state.items.filter { item ->
        val startTime = parseToLocalDateTime(item.startTime)
        extractDate(startTime) == date
    }

    val isFirst = itemsForToday.isEmpty()
    val isNotOngoing = itemsForToday.isEmpty() || !state.items.last().ongoing

    if (isFirst) {
        onEvent(ItemEvent.SetStart(start = LocalDateTime.now().toString()))
        onEvent(ItemEvent.SetOngoing(ongoing = true))
        onEvent(ItemEvent.SetPause(pause = false))
        onEvent(ItemEvent.SaveItem)

        onEvent(ItemEvent.SetIsOngoing(isOngoing = true))
    } else if (isNotOngoing) {
        onEvent(ItemEvent.SetStart(start = itemsForToday.last().endTime))
        onEvent(ItemEvent.SetEnd(end = LocalDateTime.now().toString()))
        onEvent(ItemEvent.SetOngoing(ongoing = false))
        onEvent(ItemEvent.SetPause(pause = true))
        onEvent(ItemEvent.SaveItem)

        onEvent(ItemEvent.SetStart(start = LocalDateTime.now().toString()))
        onEvent(ItemEvent.SetOngoing(ongoing = true))
        onEvent(ItemEvent.SetPause(pause = false))
        onEvent(ItemEvent.SaveItem)

        onEvent(ItemEvent.SetIsOngoing(isOngoing = true))

        ShortcutUtil.removeShortcutStopPause(context)
        ShortcutUtil.createShortcutStartPause(context)
    } else {
        onEvent(ItemEvent.SetId(id = itemsForToday.last().id))
        onEvent(ItemEvent.SetStart(start = itemsForToday.last().startTime))
        onEvent(ItemEvent.SetEnd(end = LocalDateTime.now().toString()))
        onEvent(ItemEvent.SetOngoing(ongoing = false))
        onEvent(ItemEvent.SaveItem)

        onEvent(ItemEvent.SetIsOngoing(isOngoing = false))

        ShortcutUtil.removeShortcutStartPause(context)
        ShortcutUtil.addShortcutStopPause(context)
    }
}

/*@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun HomeTabPreview() {
    com.pablos.overload.ui.tabs.HomeTab()
}
*/
