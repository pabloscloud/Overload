package com.example.reply.ui

import androidx.compose.runtime.Composable
import com.example.reply.data.item.ItemEvent
import com.example.reply.data.item.ItemState

data class TabItem(
    val titleResId: Int,
    val screen: @Composable (state: ItemState, onEvent: (ItemEvent) -> Unit) -> Unit
) {
}