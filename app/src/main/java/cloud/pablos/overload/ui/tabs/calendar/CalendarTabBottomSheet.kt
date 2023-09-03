package cloud.pablos.overload.ui.tabs.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cloud.pablos.overload.R
import cloud.pablos.overload.data.item.ItemState
import cloud.pablos.overload.ui.views.DayViewItemNotOngoing
import cloud.pablos.overload.ui.views.DayViewItemOngoing
import cloud.pablos.overload.ui.views.extractDate
import cloud.pablos.overload.ui.views.parseToLocalDateTime
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun CalendarTabBottomSheet(
    state: ItemState,
    date: LocalDate,
) {
    val itemsForSelectedDay = state.items.filter { item ->
        val startTime = parseToLocalDateTime(item.startTime)
        extractDate(startTime) == date
    }

    val itemsForSelectedDayAsc = itemsForSelectedDay.sortedByDescending { it.startTime }

    if (itemsForSelectedDayAsc.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            val itemSize = itemsForSelectedDayAsc.size
            items(itemSize) { index ->
                val isFirstItem = index == 0
                val isLastItem = index == itemSize - 1

                val item = itemsForSelectedDayAsc[index]
                Box(
                    modifier = Modifier
                        .padding(10.dp, if (isFirstItem) 10.dp else 0.dp, 10.dp, if (isLastItem) 80.dp else 10.dp),
                ) {
                    when (!item.ongoing && item.endTime.isNotBlank()) {
                        true -> DayViewItemNotOngoing(item, isSelected = false)
                        else -> DayViewItemOngoing(item, isSelected = false)
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = stringResource(id = R.string.no_items_title),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = stringResource(id = R.string.no_items_subtitle),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
}
/*
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun CalendarTabBottomSheetPreview(){
    DayView()
}*/
