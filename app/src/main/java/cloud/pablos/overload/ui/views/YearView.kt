package cloud.pablos.overload.ui.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cloud.pablos.overload.data.item.ItemEvent
import cloud.pablos.overload.data.item.ItemState
import cloud.pablos.overload.ui.tabs.calendar.WeekDaysHeader
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun YearView(year: Int, state: ItemState, onEvent: (ItemEvent) -> Unit, bottomPadding: Dp) {
    val currentYear = LocalDate.now().year
    val months = if (year == currentYear) {
        Month.values().takeWhile { it <= LocalDate.now().month }.reversed()
    } else {
        Month.values().reversed()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        item {
            WeekDaysHeader()
        }
        months.forEachIndexed { monthIndex, month ->
            item {
                MonthNameHeader(month)
            }
            val firstDayOfMonth = LocalDate.of(year, month, 1)
            val emptyCells = (firstDayOfMonth.dayOfWeek.value + 6) % 7
            val daysInMonth = firstDayOfMonth.month.length(firstDayOfMonth.isLeapYear) + emptyCells
            val weeksInMonth = daysInMonth / 7 + if (daysInMonth % 7 > 0) 1 else 0
            val isLastMonth = monthIndex == months.lastIndex

            (0 until weeksInMonth).reversed().forEachIndexed { weekIndex, weekOfMonth ->
                val isLastWeekInLastMonth = isLastMonth && weeksInMonth - weekIndex == 1

                item {
                    Box(Modifier.padding(0.dp, 0.dp, 0.dp, if (isLastWeekInLastMonth) bottomPadding else 0.dp)) {
                        WeekRow(firstDayOfMonth, weekOfMonth, state, onEvent)
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthNameHeader(month: Month) {
    Text(
        text = month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()),
        fontSize = 24.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekRow(startDayOfMonth: LocalDate, weekOfMonth: Int, state: ItemState, onEvent: (ItemEvent) -> Unit) {
    var startOfWeek = startDayOfMonth.plusWeeks(weekOfMonth.toLong())
    val emptyCells = (startOfWeek.dayOfWeek.value + 6) % 7

    startOfWeek = if (weekOfMonth == 0) startOfWeek else startOfWeek.minusDays((emptyCells).toLong())
    val endDayOfWeek = if (weekOfMonth == 0) startOfWeek.plusDays((7 - emptyCells).toLong()) else startOfWeek.plusDays((7).toLong())

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        if (weekOfMonth == 0) {
            repeat(emptyCells) {
                DayCell(date = startOfWeek, empty = true, selected = false, onEvent = onEvent)
            }
        }

        var currentDate = startOfWeek
        while (currentDate < endDayOfWeek) {
            if (currentDate.month == startDayOfMonth.month) {
                DayCell(date = currentDate, empty = false, selected = currentDate == getLocalDate(state.selectedDay), onEvent = onEvent)
            } else {
                DayCell(date = currentDate, empty = true, selected = currentDate == getLocalDate(state.selectedDay), onEvent = onEvent)
            }
            currentDate = currentDate.plusDays(1)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayCell(date: LocalDate, empty: Boolean, selected: Boolean, onEvent: (ItemEvent) -> Unit) {
    val backgroundColor = if (empty || date > LocalDate.now()) {
        Color.Transparent
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val borderColor = if (!empty && selected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }

    val string = when (empty) {
        true -> ""
        else -> date.dayOfMonth.toString()
    }

    Box(
        modifier = Modifier
            .padding(4.dp)
            .width(36.dp)
            .height(36.dp)
            .background(backgroundColor, shape = CircleShape)
            .combinedClickable(
                enabled = date <= LocalDate.now() && !empty,
                onClick = {
                    onEvent(ItemEvent.SetSelectedDay(getFormattedDate(date)))
                    onEvent(ItemEvent.SetIsSelected(isSelected = true))
                },
                indication = rememberRipple(
                    radius = 18.dp,
                ),
                interactionSource = remember { MutableInteractionSource() },
            )
            .clip(CircleShape)
            .border(2.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = string, fontSize = 14.sp)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getFormattedDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return date.format(formatter)
}

/*
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun YearViewPreview(){
    YearView()
}*/
