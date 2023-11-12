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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cloud.pablos.overload.data.item.Item
import cloud.pablos.overload.data.item.ItemEvent
import cloud.pablos.overload.data.item.ItemState
import cloud.pablos.overload.ui.tabs.configurations.OlSharedPreferences
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun YearView(year: Int, state: ItemState, onEvent: (ItemEvent) -> Unit, bottomPadding: Dp) {
    val currentYear = LocalDate.now().year
    val months = if (year == currentYear) {
        Month.entries.toTypedArray().takeWhile { it <= LocalDate.now().month }.reversed()
    } else {
        Month.entries.reversed()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
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

@Composable
fun MonthNameHeader(month: Month) {
    TextView(
        text = month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()),
        fontSize = 24.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    )
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun WeekRow(firstDayOfMonth: LocalDate, weekOfMonth: Int, state: ItemState, onEvent: (ItemEvent) -> Unit) {
    var startOfWeek = firstDayOfMonth.plusWeeks(weekOfMonth.toLong())
    val emptyCells = (startOfWeek.dayOfWeek.value + 6) % 7

    startOfWeek = if (weekOfMonth == 0) startOfWeek else startOfWeek.minusDays((emptyCells).toLong())
    val endDayOfWeek = if (weekOfMonth == 0) startOfWeek.plusDays((7 - emptyCells).toLong()) else startOfWeek.plusDays((7).toLong())

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        if (weekOfMonth == 0) {
            repeat(emptyCells) {
                EmptyDayCell()
            }
        }

        var iterationDate = startOfWeek
        val today = LocalDate.now()

        while (iterationDate < endDayOfWeek) {
            val (backgroundColor, borderColor) = getColorOfDay(
                date = iterationDate,
                firstDayOfMonth = firstDayOfMonth,
                state = state,
            )
            val number = iterationDate.dayOfMonth.toString()
            val clickable = iterationDate <= today

            DayCell(
                date = iterationDate,
                onEvent = onEvent,
                backgroundColor = backgroundColor,
                borderColor = borderColor,
                number = number,
                clickable = clickable,
            )

            iterationDate = iterationDate.plusDays(1)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun DayCell(
    date: LocalDate,
    onEvent: (ItemEvent) -> Unit,
    backgroundColor: Color,
    borderColor: Color,
    number: String,
    clickable: Boolean,
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .width(36.dp)
            .height(36.dp)
            .background(backgroundColor, shape = CircleShape)
            .combinedClickable(
                enabled = clickable,
                onClick = {
                    onEvent(ItemEvent.SetSelectedDayCalendar(getFormattedDate(date)))
                    onEvent(ItemEvent.SetIsSelectedHome(isSelected = true))
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
        TextView(
            text = number,
            fontSize = 14.sp,
        )
    }
}

@Composable
fun EmptyDayCell() {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .width(36.dp)
            .height(36.dp)
            .background(Color.Transparent, shape = CircleShape)
            .clip(CircleShape)
            .border(2.dp, Color.Transparent, CircleShape),
    )
}

fun getFormattedDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return date.format(formatter)
}

@Composable
fun getColorOfDay(date: LocalDate, firstDayOfMonth: LocalDate, state: ItemState): Pair<Color, Color> {
    val month = firstDayOfMonth.month
    val today = LocalDate.now()

    val backgroundColor = if (date <= today && date.month == month) {
        val context = LocalContext.current
        val sharedPreferences = remember { OlSharedPreferences(context) }

        val goalWork by remember { mutableIntStateOf(sharedPreferences.getWorkGoal()) }
        val goalPause by remember { mutableIntStateOf(sharedPreferences.getPauseGoal()) }

        if (goalWork > 0 && goalPause > 0) {
            val goalsReached = getProgressOfDay(date, state, goalWork, goalPause)

            if (goalsReached.goalPauseReached == true && goalsReached.goalWorkReached == true) {
                MaterialTheme.colorScheme.primaryContainer
            } else if (goalsReached.goalPauseReached == false && goalsReached.goalWorkReached == false) {
                MaterialTheme.colorScheme.errorContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    } else {
        Color.Transparent
    }

    val borderColor = if (
        date == getLocalDate(state.selectedDayCalendar) &&
        date.month == month
    ) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }

    return Pair(backgroundColor, borderColor)
}

data class GoalsReached(val goalWorkReached: Boolean?, val goalPauseReached: Boolean?)

@Composable
fun getProgressOfDay(date: LocalDate, state: ItemState, goalWork: Int, goalPause: Int): GoalsReached {
    val cachedProgress by remember(date, state.items) {
        mutableStateOf(
            computeProgressOfDay(date, state.items, goalWork, goalPause),
        )
    }

    return cachedProgress
}

private fun computeProgressOfDay(
    date: LocalDate,
    items: List<Item>,
    goalWork: Int,
    goalPause: Int,
): GoalsReached {
    val goalWorkReached: Boolean
    val goalPauseReached: Boolean

    val durationWork: Long
    val durationPause: Long
    var countWork: Long = 0
    var countPause: Long = 0

    val itemsForDay = items.filter { item ->
        val startTime = parseToLocalDateTime(item.startTime)
        extractDate(startTime) == date
    }

    // Filter items by type
    val itemsFilteredWork = itemsForDay.filter { item ->
        !item.pause
    }
    val itemsFilteredPause = itemsForDay.filter { item ->
        item.pause
    }

    // Count duration
    itemsFilteredWork.forEach {
        val parsedStartTime = parseToLocalDateTime(it.startTime)
        val parsedEndTime = if (it.ongoing) {
            LocalDateTime.now()
        } else {
            parseToLocalDateTime(it.endTime)
        }

        countWork += Duration.between(parsedStartTime, parsedEndTime).toMillis()
    }
    durationWork = countWork

    itemsFilteredPause.forEach {
        val parsedStartTime = parseToLocalDateTime(it.startTime)
        val parsedEndTime = if (it.ongoing) {
            LocalDateTime.now()
        } else {
            parseToLocalDateTime(it.endTime)
        }

        countPause += Duration.between(parsedStartTime, parsedEndTime).toMillis()
    }

    // If last item is not a pause, automatically count duration between then and now
    if (
        itemsForDay.isNotEmpty() &&
        !itemsForDay.last().pause &&
        date == LocalDate.now()
    ) {
        val parsedStartTime = parseToLocalDateTime(items.last().endTime)
        val parsedEndTime = LocalDateTime.now()

        countPause += Duration.between(parsedStartTime, parsedEndTime).toMillis()
    }
    durationPause = countPause

    goalPauseReached = durationPause >= goalPause

    goalWorkReached = durationWork <= goalWork

    return GoalsReached(goalWorkReached, goalPauseReached)
}
