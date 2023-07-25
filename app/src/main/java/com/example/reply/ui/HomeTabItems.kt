package com.example.reply.ui

import androidx.compose.foundation.layout.systemBarsPadding

val homeTabItems = listOf(
    TabItem(
        title = "Today",
        screen = { DayView() },
    ),
    TabItem(
        title = "Yesterday",
        screen = { EmptyComingSoon() }
    ),
    TabItem(
        title = "Wednesday",
        screen = { EmptyComingSoon() }
    )
)