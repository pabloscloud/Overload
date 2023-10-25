package cloud.pablos.overload.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.runtime.Composable
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

@Composable
fun OverloadNavigationFab(
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
            TextView(
                if (isOngoing) {
                    stringResource(id = R.string.stop)
                } else {
                    stringResource(id = R.string.start)
                },
            )
        },
        icon = {
            Icon(
                imageVector =
                if (isOngoing) {
                    Icons.Default.Stop
                } else {
                    Icons.Default.PlayArrow
                },
                contentDescription =
                if (isOngoing) {
                    stringResource(id = R.string.stop)
                } else {
                    stringResource(id = R.string.start)
                },
            )
        },
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        onClick = {
            startOrStopPause(state, onEvent)
        },
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp),
    )
}

/*@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun HomeTabPreview() {
    com.pablos.overload.ui.tabs.HomeTab()
}
*/
