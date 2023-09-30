package cloud.pablos.overload.ui.tabs.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cloud.pablos.overload.data.item.ItemEvent
import cloud.pablos.overload.data.item.ItemState
import cloud.pablos.overload.ui.views.DayView
import cloud.pablos.overload.ui.views.TextView
import cloud.pablos.overload.ui.views.YearView
import cloud.pablos.overload.ui.views.getLocalDate
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarTab(
    state: ItemState,
    onEvent: (ItemEvent) -> Unit,
) {
    val selectedDay = getLocalDate(state.selectedDayCalendar)
    val selectedYear by remember { mutableIntStateOf(state.selectedYearCalendar) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetScaffoldState()
    var sheetOffset by remember { mutableFloatStateOf(0f) }
    var expandSheet by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSelectedHome) {
        if (expandSheet) {
            scope.launch { sheetState.bottomSheetState.expand() }
            onEvent(ItemEvent.SetIsSelectedHome(isSelected = false))
        } else {
            expandSheet = true
        }
    }

    LaunchedEffect(selectedYear) {
        if (state.selectedYearCalendar != LocalDate.now().year) {
            onEvent(ItemEvent.SetSelectedYearCalendar(LocalDate.now().year))
        }
    }

    Scaffold(
        topBar = {
            CalendarTabTopAppBar(
                state = state,
                onEvent = onEvent,
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(paddingValues)) {
                Surface(
                    tonalElevation = NavigationBarDefaults.Elevation,
                    color = MaterialTheme.colorScheme.background,
                ) {
                    WeekDaysHeader()
                }
                BottomSheetScaffold(
                    scaffoldState = sheetState,
                    sheetContent = {
                        DayView(
                            state = state,
                            onEvent = onEvent,
                            date = selectedDay,
                            isEditable = false,
                        )
                    },
                ) { innerPadding ->
                    YearView(
                        state = state,
                        onEvent = onEvent,
                        year = state.selectedYearCalendar,
                        bottomPadding = innerPadding.calculateBottomPadding(),
                    )

                    Modifier.pointerInput(Unit) {
                        detectVerticalDragGestures { _, dragAmount ->
                            val maxOffset = 64.dp.toPx()

                            sheetOffset = (sheetOffset + dragAmount).coerceIn(0f, maxOffset)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeekDaysHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        DayOfWeekHeaderCell("M")
        DayOfWeekHeaderCell("T")
        DayOfWeekHeaderCell("W")
        DayOfWeekHeaderCell("T")
        DayOfWeekHeaderCell("F")
        DayOfWeekHeaderCell("S")
        DayOfWeekHeaderCell("S")
    }
}

@Composable
fun DayOfWeekHeaderCell(text: String) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .width(36.dp)
            .height(36.dp),
        contentAlignment = Alignment.Center,
    ) {
        TextView(
            text = text,
            fontSize = 14.sp,
        )
    }
}

/*
@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun CalendarTabPreview() {
    CalendarTab()
}
*/
