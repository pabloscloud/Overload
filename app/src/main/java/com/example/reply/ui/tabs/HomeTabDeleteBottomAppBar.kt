package com.example.reply.ui.tabs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.reply.data.item.ItemEvent
import com.example.reply.data.item.ItemState
import com.example.reply.ui.views.extractDate
import com.example.reply.ui.views.getLocalDate
import com.example.reply.ui.views.parseToLocalDateTime
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeTabBottomAppBar(
    state: ItemState,
    onEvent: (ItemEvent) -> Unit,
) {
    val date = when (state.selectedDay) {
        "" -> LocalDate.now()
        else -> getLocalDate(state.selectedDay)
    }

    val itemsForSelectedDay = state.items.filter { item ->
        val startTime = parseToLocalDateTime(item.startTime)
        extractDate(startTime) == date
    }

    if (state.isDeleting) {
        BottomAppBar(
            actions = {
                IconButton(onClick = {
                    onEvent(ItemEvent.SetSelectedItems(state.selectedItems + itemsForSelectedDay))
                }) {
                    Icon(
                        Icons.Filled.SelectAll,
                        contentDescription = null,
                    )
                }
                IconButton(onClick = {
                    onEvent(ItemEvent.SetSelectedItems(state.selectedItems - itemsForSelectedDay))
                }) {
                    Icon(
                        Icons.Filled.Deselect,
                        contentDescription = null,
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        onEvent(ItemEvent.DeleteItems(state.selectedItems))
                    },
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                ) {
                    Icon(Icons.Filled.DeleteForever, "Localized description")
                }
            },
        )
    }
}

/*@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun HomeTabPreview() {
    com.example.reply.ui.tabs.HomeTab()
}
*/
