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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.reply.data.item.ItemEvent
import com.example.reply.data.item.ItemState
import com.example.reply.ui.tabs.homeTabItems
import com.example.reply.ui.utils.ReplyNavigationType
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeTab(
    navigationType: ReplyNavigationType,
    state: ItemState,
    onEvent: (ItemEvent) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = 2,
        initialPageOffsetFraction = 0f
    ) {
        homeTabItems.size
    }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Overload")},
                colors = TopAppBarDefaults.topAppBarColors (
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
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
                ) {
                    homeTabItems.forEachIndexed { index, item ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                            text = {
                                Text(
                                    text = stringResource(id = item.titleResId),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        )
                    }
                }
                HorizontalPager(
                    state = pagerState,
                ){
                    homeTabItems[pagerState.currentPage].screen(state = state, onEvent = onEvent)
                }
            }
            Box(modifier = Modifier.align(Alignment.BottomEnd)){
                if(navigationType == ReplyNavigationType.BOTTOM_NAVIGATION) {
                    HomeTabFab(state = state, onEvent = onEvent)
                }
            }
        }
    }
}

/*
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun HomeTabPreview() {
    HomeTab()
}
*/