package com.example.reply.ui

import androidx.compose.runtime.Composable

data class TabItem (
    val title: String,
    val screen: @Composable () -> Unit
)