package cloud.pablos.overload.ui.tabs.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cloud.pablos.overload.R
import cloud.pablos.overload.data.item.ItemEvent
import cloud.pablos.overload.data.item.ItemState
import cloud.pablos.overload.ui.views.TextView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTabDeleteTopAppBar(
    state: ItemState,
    onEvent: (ItemEvent) -> Unit,
) {
    TopAppBar(
        title = {
            TextView(
                text = state.selectedItemsHome.size.toString() + " " + stringResource(id = R.string.itemCount_selected),
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        actions = {
            IconButton(onClick = {
                onEvent(ItemEvent.SetIsDeletingHome(false))
            }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(id = R.string.get_out_of_isDeleting),
                    tint = MaterialTheme.colorScheme.primaryContainer,
                )
            }
        },
    )
}

/*@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun HomeTabPreview() {
    com.pablos.overload.ui.tabs.HomeTab()
}
*/
