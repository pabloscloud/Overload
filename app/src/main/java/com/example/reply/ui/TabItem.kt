package com.example.reply.ui

import androidx.compose.runtime.Composable
import com.example.reply.data.ItemEvent
import com.example.reply.data.ItemState

data class TabItem(
    val title: String,
    val screen: @Composable (state: ItemState, onEvent: (ItemEvent) -> Unit) -> Unit
) {
}