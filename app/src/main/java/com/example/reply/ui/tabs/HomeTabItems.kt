package com.example.reply.ui.tabs

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.reply.data.Item
import com.example.reply.ui.views.DayView
import com.example.reply.ui.EmptyComingSoon
import com.example.reply.ui.TabItem
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
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