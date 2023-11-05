package cloud.pablos.overload.ui.views

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cloud.pablos.overload.R
import cloud.pablos.overload.data.item.Item
import cloud.pablos.overload.ui.tabs.home.HomeTabProgress
import cloud.pablos.overload.ui.tabs.home.ProgressData
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

@SuppressLint("UnusedTransitionTargetStateParameter")
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun DayViewProgress(
    items: List<Item>,
    goal: Int,
    date: LocalDate = LocalDate.now(),
    isPause: Boolean,
) {
    val duration: Long
    var count: Long = 0

    // Filter items by type
    val itemsFiltered = items.filter { item ->
        when (isPause) {
            true -> {
                item.pause
            }
            false -> {
                !item.pause
            }
        }
    }

    // Count duration
    itemsFiltered.forEach {
        val parsedStartTime = parseToLocalDateTime(it.startTime)
        val parsedEndTime = if (it.ongoing) {
            LocalDateTime.now()
        } else {
            parseToLocalDateTime(it.endTime)
        }

        count += Duration.between(parsedStartTime, parsedEndTime).toMillis()
    }

    // If last item is not a pause, automatically count duration between then and now
    if (
        isPause &&
        !items.last().pause &&
        date == LocalDate.now()
    ) {
        val parsedStartTime = parseToLocalDateTime(items.last().endTime)
        val parsedEndTime = LocalDateTime.now()

        count += Duration.between(parsedStartTime, parsedEndTime).toMillis()
    }
    duration = count

    // Animation
    val transition = updateTransition(targetState = duration, label = "progress")

    // Progress
    val progress = transition.animateFloat(
        transitionSpec = { tween(800) },
        label = "progress",
    ) { remTime ->
        val calculatedProgress = if (remTime < 0) {
            360f
        } else {
            360f - ((360f / goal) * (goal - remTime))
        }

        calculatedProgress.coerceAtMost(360f)
    }

    // Color
    val color = transition.animateColor(
        transitionSpec = {
            tween(800, easing = LinearEasing)
        },
        label = "Color transition",
    ) {
        if (progress.value < 360f) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.primary
        }
    }

    // Text
    val title = when (isPause) {
        true -> {
            stringResource(id = R.string.pause_left)
        }
        false -> {
            stringResource(id = R.string.work_left)
        }
    }
    val subtitle = getDurationString(Duration.ofMillis(goal - duration))

    HomeTabProgress(
        progressData = ProgressData(progress = progress, color = color),
        title = title,
        subtitle = subtitle,
    )
}
