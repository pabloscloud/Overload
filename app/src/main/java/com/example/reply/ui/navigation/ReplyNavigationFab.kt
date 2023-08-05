package com.example.reply.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.reply.data.item.ItemEvent
import com.example.reply.data.item.ItemState
import com.example.reply.ui.views.extractDate
import com.example.reply.ui.views.getLocalDate
import com.example.reply.ui.views.parseToLocalDateTime
import java.time.LocalDate
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReplyNavigationFab(
    state: ItemState,
    onEvent: (ItemEvent) -> Unit,
) {
    val date = LocalDate.now()

    val itemsForToday = state.items.filter { item ->
        val startTime = parseToLocalDateTime(item.startTime)
        extractDate(startTime) == date
    }

    val isOngoing = itemsForToday.isNotEmpty() && itemsForToday.last().ongoing

    NavigationDrawerItem(
        selected = false,
        label = {
            Text(
                if (isOngoing) {
                    "Pause"
                } else {
                    "Start"
                },
            )
        },
        icon = {
            Icon(
                imageVector =
                if (isOngoing) {
                    Icons.Default.Pause
                } else {
                    Icons.Default.PlayArrow
                },
                contentDescription =
                if (isOngoing) {
                    "Pause"
                } else {
                    "Start"
                },
            )
        },
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
        ),
        onClick = {
            val isFirst = itemsForToday.isEmpty()
            val isNotOngoing = itemsForToday.isEmpty() || !itemsForToday.last().ongoing

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
            .padding(top = 8.dp, bottom = 8.dp),
    )
}

/*@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun HomeTabPreview() {
    com.example.reply.ui.tabs.HomeTab()
}
*/
