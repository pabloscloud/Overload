package com.example.reply.ui.tabs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.reply.data.item.ItemEvent
import com.example.reply.data.item.ItemState

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeTabDeleteTopAppBar(
    state: ItemState,
    onEvent: (ItemEvent) -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = state.selectedItems.size.toString() + " selected",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Normal,
                ),
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        actions = {
            IconButton(onClick = {
                onEvent(ItemEvent.SetIsDeleting(false))
            }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Localized description",
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
    com.example.reply.ui.tabs.HomeTab()
}
*/
