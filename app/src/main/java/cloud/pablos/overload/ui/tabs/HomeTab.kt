package cloud.pablos.overload.ui.tabs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cloud.pablos.overload.data.item.ItemEvent
import cloud.pablos.overload.data.item.ItemState
import cloud.pablos.overload.ui.utils.OverloadNavigationType
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeTab(
    navigationType: OverloadNavigationType,
    state: ItemState,
    onEvent: (ItemEvent) -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = 2,
        initialPageOffsetFraction = 0f,
    ) {
        homeTabItems.size
    }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        val selectedDayString = when (pagerState.currentPage) {
            0 -> getFormattedDate(daysBefore = 2)
            1 -> getFormattedDate(daysBefore = 1)
            2 -> getFormattedDate()
            else -> getFormattedDate()
        }
        onEvent(ItemEvent.SetSelectedDay(selectedDayString))
    }

    Scaffold(
        topBar = {
            when (state.isDeleting) {
                true -> HomeTabDeleteTopAppBar(state = state, onEvent = onEvent)
                else -> HomeTabTopAppBar()
            }
        },
        bottomBar = {
            HomeTabBottomAppBar(state = state, onEvent = onEvent)
        },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(paddingValues)) {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    indicator = { tabPositions ->
                        if (pagerState.currentPage < tabPositions.size) {
                            TabRowDefaults.PrimaryIndicator(
                                modifier = Modifier
                                    .tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                shape = RoundedCornerShape(
                                    topStart = 3.dp,
                                    topEnd = 3.dp,
                                    bottomEnd = 0.dp,
                                    bottomStart = 0.dp,
                                ),
                            )
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    divider = {},
                ) {
                    homeTabItems.forEachIndexed { index, item ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch { pagerState.animateScrollToPage(index) }
                            },
                            text = {
                                Text(
                                    text = stringResource(id = item.titleResId),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            },
                        )
                    }
                }
                HorizontalPager(
                    state = pagerState,
                ) {
                    homeTabItems[pagerState.currentPage].screen(state = state, onEvent = onEvent)
                }
            }
            Box(modifier = Modifier.align(Alignment.BottomEnd)) {
                if (navigationType == OverloadNavigationType.BOTTOM_NAVIGATION) {
                    HomeTabFab(state = state, onEvent = onEvent)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getFormattedDate(daysBefore: Long): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.now().minusDays(daysBefore)
    return date.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getFormattedDate(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.now()
    return date.format(formatter)
}

/*
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun HomeTabPreview() {
    com.pablos.overload.ui.tabs.HomeTab()
}
*/
