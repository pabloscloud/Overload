package cloud.pablos.overload.ui.tabs.home

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
import androidx.compose.ui.res.stringResource
import cloud.pablos.overload.R
import cloud.pablos.overload.data.item.ItemEvent
import cloud.pablos.overload.data.item.ItemState
import cloud.pablos.overload.ui.views.extractDate
import cloud.pablos.overload.ui.views.getLocalDate
import cloud.pablos.overload.ui.views.parseToLocalDateTime
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
                    onEvent(ItemEvent.SetSelectedItems(state.selectedItems + itemsForSelectedDay.filterNot { state.selectedItems.contains(it) }))
                }) {
                    Icon(
                        Icons.Filled.SelectAll,
                        contentDescription = stringResource(id = R.string.select_all_items_of_selected_day),
                    )
                }
                IconButton(onClick = {
                    onEvent(ItemEvent.SetSelectedItems(state.selectedItems - itemsForSelectedDay))
                }) {
                    Icon(
                        Icons.Filled.Deselect,
                        contentDescription = stringResource(id = R.string.deselect_all_items_of_selected_day),
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
                    Icon(
                        Icons.Filled.DeleteForever,
                        contentDescription = stringResource(id = R.string.delete_items_forever),
                    )
                }
            },
        )
    }
}

/*@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun HomeTabPreview() {
    com.pablos.overload.ui.tabs.HomeTab()
}
*/
