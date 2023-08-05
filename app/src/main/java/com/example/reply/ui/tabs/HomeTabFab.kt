package com.example.reply.ui.tabs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.reply.R
import com.example.reply.data.item.ItemEvent
import com.example.reply.data.item.ItemState
import com.example.reply.ui.views.extractDate
import com.example.reply.ui.views.getLocalDate
import com.example.reply.ui.views.parseToLocalDateTime
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

    FloatingActionButton(
        onClick = {
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
            } else {
                onEvent(ItemEvent.SetId(id = itemsForToday.last().id))
                onEvent(ItemEvent.SetStart(start = itemsForToday.last().startTime))
                onEvent(ItemEvent.SetEnd(end = LocalDateTime.now().toString()))
                onEvent(ItemEvent.SetOngoing(ongoing = false))
                onEvent(ItemEvent.SaveItem)

                onEvent(ItemEvent.SetIsOngoing(isOngoing = false))
            }
        },
        modifier = Modifier
            .padding(10.dp),
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
    ) {
        val isOngoing = itemsForToday.isNotEmpty() && itemsForToday.last().ongoing
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp),
        ) {
            Icon(
                imageVector =
                if (isOngoing) {
                    Icons.Default.Pause
                } else {
                    Icons.Default.PlayArrow
                },
                contentDescription = stringResource(id = R.string.edit),
                modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp),

            )
            Text(
                text =
                if (isOngoing) {
                    "Pause"
                } else {
                    "Start"
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
    com.example.reply.ui.tabs.HomeTab()
}
*/
