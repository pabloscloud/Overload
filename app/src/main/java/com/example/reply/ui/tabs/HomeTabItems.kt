package com.example.reply.ui.tabs

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.reply.ui.EmptyComingSoon
import com.example.reply.ui.TabItem
import com.example.reply.ui.views.DayView

@RequiresApi(Build.VERSION_CODES.O)
val homeTabItems = listOf(
    TabItem(
        title = "Today",
        screen = { state, onEvent -> DayView(state, onEvent) }
    ),
    TabItem(
        title = "Yesterday",
        screen = { _, _ -> EmptyComingSoon() }
    ),
    TabItem(
        title = "Wednesday",
        screen = { _, _ -> EmptyComingSoon() }
    )
)