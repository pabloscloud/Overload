package cloud.pablos.overload.ui.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cloud.pablos.overload.R
import cloud.pablos.overload.data.item.Item
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun DayViewItemNotOngoing(item: Item, isSelected: Boolean) {
    var backgroundColor: Color
    var foregroundColor: Color

    val parsedStartTime: LocalDateTime
    val parsedEndTime: LocalDateTime

    item.let {
        parsedStartTime = parseDateTime(it.startTime)
        parsedEndTime = parseDateTime(it.endTime)
        when (isSelected) {
            true -> {
                backgroundColor = MaterialTheme.colorScheme.errorContainer
                foregroundColor = MaterialTheme.colorScheme.onErrorContainer
            }

            false -> {
                when (it.pause) {
                    true -> {
                        backgroundColor = MaterialTheme.colorScheme.onSecondaryContainer
                        foregroundColor = MaterialTheme.colorScheme.secondaryContainer
                    }

                    false -> {
                        backgroundColor = MaterialTheme.colorScheme.secondaryContainer
                        foregroundColor = MaterialTheme.colorScheme.onSecondaryContainer
                    }
                }
            }
        }
    }

    val startTimeString = parsedStartTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    var endTimeString = parsedEndTime.format(DateTimeFormatter.ofPattern("HH:mm"))

    var duration = Duration.between(parsedStartTime, parsedEndTime)
    val hours = duration.toHours()
    val minutes = duration.toMinutesPart()
    val seconds = duration.toSecondsPart()
    val durationString = when {
        hours > 0 -> "$hours h $minutes min"
        minutes > 0 -> "$minutes min $seconds sec"
        else -> "$seconds sec"
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(backgroundColor),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = startTimeString,
                maxLines = 1,
                color = foregroundColor,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(12.5.dp),
            )

            if (item.pause) {
                Icon(
                    Icons.Outlined.DarkMode,
                    contentDescription = stringResource(id = R.string.pause),
                    tint = foregroundColor,
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
                    .offset(x = 5.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp)
                            .background(foregroundColor),
                    )
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = stringResource(id = R.string.arrow_forward),
                        tint = foregroundColor,
                        modifier = Modifier
                            .offset(x = (-5).dp)
                            .size(25.dp),
                    )
                }
            }

            Text(
                text = endTimeString,
                maxLines = 1,
                color = foregroundColor,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(12.5.dp),
            )
        }

        Text(
            text = durationString,
            color = foregroundColor,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .background(backgroundColor)
                .padding(8.dp),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun parseDateTime(dateTimeString: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    return LocalDateTime.parse(dateTimeString, formatter)
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun DayViewItemPreview() {
    val exampleItem = Item(
        startTime = LocalDateTime.of(2023, 7, 29, 11, 57).toString(),
        endTime = LocalDateTime.of(2023, 7, 29, 12, 31).toString(),
        pause = true,
        ongoing = false,
    )

    DayViewItemNotOngoing(exampleItem, isSelected = false)
}
