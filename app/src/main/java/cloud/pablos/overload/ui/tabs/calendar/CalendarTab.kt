package cloud.pablos.overload.ui.tabs.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import cloud.pablos.overload.ui.tabs.home.getFormattedDate
import cloud.pablos.overload.ui.utils.OverloadContentType
import cloud.pablos.overload.ui.views.DayView
import cloud.pablos.overload.ui.views.TextView
import cloud.pablos.overload.ui.views.YearView
import cloud.pablos.overload.ui.views.getLocalDate
import cloud.pablos.overload.ui.views.parseToLocalDateTime
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CalendarTab(
    contentType: OverloadContentType,
    state: ItemState,
    onEvent: (ItemEvent) -> Unit,
) {
    val selectedDay = getLocalDate(state.selectedDayCalendar)
    val selectedYear by remember { mutableIntStateOf(state.selectedYearCalendar) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetScaffoldState()
    var sheetOffset by remember { mutableFloatStateOf(0f) }
    var expandSheet by remember { mutableStateOf(false) }

    val firstYear = if (state.items.isEmpty()) {
        LocalDate.now().year
    } else {
        state.items.minByOrNull { it.startTime }?.let { parseToLocalDateTime(it.startTime).year } ?: LocalDate.now().year
    }

    val firstDay = LocalDate.of(firstYear, 1, 1)
    val lastDay = LocalDate.now()
    val daysCount = ChronoUnit.DAYS.between(firstDay, lastDay).toInt() + 1

    val pagerState = rememberPagerState(
        initialPage = daysCount,
        initialPageOffsetFraction = 0f,
        pageCount = { daysCount },
    )

    LaunchedEffect(state.isSelectedHome) {
        if (expandSheet) {
            scope.launch { sheetState.bottomSheetState.expand() }
            pagerState.scrollToPage(ChronoUnit.DAYS.between(firstDay, selectedDay).toInt())
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

    LaunchedEffect(pagerState.currentPage) {
        onEvent(
            ItemEvent.SetSelectedDayCalendar(
                LocalDate.now()
                    .minusDays((daysCount - pagerState.currentPage - 1).toLong())
                    .toString(),
            ),
        )

        if (selectedYear != selectedDay.year) {
            onEvent(ItemEvent.SetSelectedYearCalendar(selectedDay.year))
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
                AnimatedVisibility(visible = contentType == OverloadContentType.DUAL_PANE) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier.weight(1f),
                        ) {
                            Column {
                                Surface(
                                    tonalElevation = NavigationBarDefaults.Elevation,
                                    color = MaterialTheme.colorScheme.background,
                                ) {
                                    WeekDaysHeader()
                                }

                                YearView(
                                    state = state,
                                    onEvent = onEvent,
                                    year = state.selectedYearCalendar,
                                    bottomPadding = 0.dp,
                                )
                            }
                        }

                        Box(
                            modifier = Modifier.weight(1f),
                        ) {
                            HorizontalPager(
                                state = pagerState,
                            ) {
                                Column {
                                    Surface(
                                        tonalElevation = NavigationBarDefaults.Elevation,
                                        color = MaterialTheme.colorScheme.background,
                                    ) {
                                        DateHeader(selectedDay)
                                    }

                                    DayView(
                                        state = state,
                                        onEvent = onEvent,
                                        date = selectedDay,
                                        isEditable = false,
                                    )
                                }
                            }
                        }
                    }
                }
                AnimatedVisibility(visible = contentType == OverloadContentType.SINGLE_PANE) {
                    Column {
                        Surface(
                            tonalElevation = NavigationBarDefaults.Elevation,
                            color = MaterialTheme.colorScheme.background,
                        ) {
                            WeekDaysHeader()
                        }

                        BottomSheetScaffold(
                            scaffoldState = sheetState,
                            sheetContent = {
                                HorizontalPager(
                                    state = pagerState,
                                ) {
                                    DayView(
                                        state = state,
                                        onEvent = onEvent,
                                        date = selectedDay,
                                        isEditable = false,
                                    )
                                }
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

@Composable
fun DateHeader(date: LocalDate) {
    val text = getFormattedDate(date, true)
    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(36.dp)
            .fillMaxWidth(),
    ) {
        TextView(
            text = text,
            fontSize = 14.sp,
            modifier = Modifier.padding(6.dp),
        )
    }
}
