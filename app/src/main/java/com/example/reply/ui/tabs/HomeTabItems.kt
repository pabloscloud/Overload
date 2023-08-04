package com.example.reply.ui.tabs

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.reply.R
import com.example.reply.ui.EmptyComingSoon
import com.example.reply.ui.TabItem
import com.example.reply.ui.views.DayView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
val daysBeforeYesterday = Calendar.getInstance().apply {
    time = Date()
    add(Calendar.DATE, -2) // Subtract 2 days
}
@RequiresApi(Build.VERSION_CODES.O)
val dayBeforeYesterday = dateFormat.format(daysBeforeYesterday.time)

@RequiresApi(Build.VERSION_CODES.O)
val dayBeforeYesterdayResId = when (dayBeforeYesterday) {
    "Monday" -> R.string.monday
    "Tuesday" -> R.string.tuesday
    "Wednesday" -> R.string.wednesday
    "Thursday" -> R.string.thursday
    "Friday" -> R.string.friday
    "Saturday" -> R.string.saturday
    "Sunday" -> R.string.sunday
    else -> R.string.unknown_day // Add a default string resource in case the day is not matched
}

@RequiresApi(Build.VERSION_CODES.O)
val homeTabItems = listOf(
    TabItem(
        titleResId = dayBeforeYesterdayResId,
        screen = { state, onEvent -> DayView(state, onEvent) }
    ),
    TabItem(
        titleResId = R.string.yesterday,
        screen = { state, onEvent -> DayView(state, onEvent) }
    ),
    TabItem(
        titleResId = R.string.today,
        screen = { state, onEvent -> DayView(state, onEvent) }
    )
)