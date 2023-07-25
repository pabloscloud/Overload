import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.reply.R
import com.example.reply.ui.homeTabItems
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeTab() {
    val pagerState = rememberPagerState(
        initialPage = 0,
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
                                    text = item.title,
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
                    homeTabItems[pagerState.currentPage].screen()
                }
            }
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                FloatingActionButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(id = R.string.edit),
                        modifier = Modifier.padding(18.dp)
                    )
                }
            }
        }
    }
    /*val pagerState = rememberPagerState()
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
        Box() {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                BoxWithConstraints() {
            TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        indicator = { tabPositions ->
                            if (pagerState.currentPage < tabPositions.size) {
                                PrimaryIndicator(
                                    modifier = Modifier
                                        .tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                    //width = maxWidth / homeTabItems.size / 2,
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
                                selected = index == pagerState.currentPage,
                                text = {
                                    Text(
                                        text = item.title,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                },
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(
                                            index
                                        )
                                    }
                                },
                                modifier = Modifier.background(color = MaterialTheme.colorScheme.secondaryContainer)
                            )
                        }
                    }
                }
                HorizontalPager(
                    pageCount = homeTabItems.size,
                    state = pagerState
                ) {
                    homeTabItems[pagerState.currentPage].screen()
                }
            }
            FloatingActionButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    //.align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.edit),
                    modifier = Modifier.padding(18.dp)
                )
            }
        }
    }*/
}

/*
@Composable
fun PrimaryIndicator(
    modifier: Modifier = Modifier,
    width: Dp = 24.dp,
    height: Dp = 3.0.dp,
    color: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    shape: RoundedCornerShape = RoundedCornerShape(3.0.dp)
) {
    Spacer(
        modifier
            .requiredSize(width, height)
            .background(color = color, shape = shape)
    )
}
*/
@Preview
@Composable
fun HomeTabPreview() {
    HomeTab()
}
