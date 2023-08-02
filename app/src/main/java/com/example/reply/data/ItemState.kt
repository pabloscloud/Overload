package com.example.reply.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class ItemState(
    val items: List<Item> = emptyList(),
    val id: Int = 0,
    val start: String = LocalDateTime.now().toString(),
    val end: String = "",
    val ongoing: Boolean = false,
    val pause: Boolean = false,
    val isOngoing: Boolean = false
)