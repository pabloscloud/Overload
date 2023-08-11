package cloud.pablos.overload.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import cloud.pablos.overload.data.item.ItemEvent
import cloud.pablos.overload.data.item.ItemState
import cloud.pablos.overload.ui.navigation.ModalNavigationDrawerContent
import cloud.pablos.overload.ui.navigation.OverloadBottomNavigationBar
import cloud.pablos.overload.ui.navigation.OverloadNavigationActions
import cloud.pablos.overload.ui.navigation.OverloadNavigationRail
import cloud.pablos.overload.ui.navigation.OverloadRoute
import cloud.pablos.overload.ui.navigation.OverloadTopLevelDestination
import cloud.pablos.overload.ui.navigation.PermanentNavigationDrawerContent
import cloud.pablos.overload.ui.tabs.home.HomeTab
import cloud.pablos.overload.ui.utils.DevicePosture
import cloud.pablos.overload.ui.utils.OverloadNavigationContentPosition
import cloud.pablos.overload.ui.utils.OverloadNavigationType
import cloud.pablos.overload.ui.utils.isBookPosture
import cloud.pablos.overload.ui.utils.isSeparating
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun OverloadApp(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    state: ItemState,
    onEvent: (ItemEvent) -> Unit,
) {
    /**
     * This will help us select type of navigation and content type depending on window size and
     * fold state of the device.
     */
    val navigationType: OverloadNavigationType

    /**
     * We are using display's folding features to map the device postures a fold is in.
     * In the state of folding device If it's half fold in BookPosture we want to avoid content
     * at the crease/hinge
     */
    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()

    val foldingDevicePosture = when {
        isBookPosture(foldingFeature) ->
            DevicePosture.BookPosture(foldingFeature.bounds)

        isSeparating(foldingFeature) ->
            DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)

        else -> DevicePosture.NormalPosture
    }

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            navigationType = OverloadNavigationType.BOTTOM_NAVIGATION
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = OverloadNavigationType.NAVIGATION_RAIL
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
                OverloadNavigationType.NAVIGATION_RAIL
            } else {
                OverloadNavigationType.PERMANENT_NAVIGATION_DRAWER
            }
        }
        else -> {
            navigationType = OverloadNavigationType.BOTTOM_NAVIGATION
        }
    }

    /**
     * Content inside Navigation Rail/Drawer can also be positioned at top, bottom or center for
     * ergonomics and reachability depending upon the height of the device.
     */
    val navigationContentPosition = when (windowSize.heightSizeClass) {
        WindowHeightSizeClass.Compact -> {
            OverloadNavigationContentPosition.TOP
        }
        WindowHeightSizeClass.Medium,
        WindowHeightSizeClass.Expanded,
        -> {
            OverloadNavigationContentPosition.TOP
        }
        else -> {
            OverloadNavigationContentPosition.TOP
        }
    }

    OverloadNavigationWrapper(
        navigationType = navigationType,
        navigationContentPosition = navigationContentPosition,
        state = state,
        onEvent = onEvent,
    )
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun OverloadNavigationWrapper(
    navigationType: OverloadNavigationType,
    navigationContentPosition: OverloadNavigationContentPosition,
    state: ItemState,
    onEvent: (ItemEvent) -> Unit,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        OverloadNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination =
        navBackStackEntry?.destination?.route ?: OverloadRoute.HOME

    if (navigationType == OverloadNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        // TODO check on custom width of PermanentNavigationDrawer: b/232495216
        PermanentNavigationDrawer(drawerContent = {
            PermanentNavigationDrawerContent(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                state = state,
            )
        }) {
            OverloadAppContent(
                navigationType = navigationType,
                navigationContentPosition = navigationContentPosition,
                navController = navController,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                state = state,
                onEvent = onEvent,
            )
        }
    } else {
        ModalNavigationDrawer(
            drawerContent = {
                ModalNavigationDrawerContent(
                    selectedDestination = selectedDestination,
                    navigationContentPosition = navigationContentPosition,
                    navigateToTopLevelDestination = navigationActions::navigateTo,
                    onDrawerClicked = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    state = state,
                    onEvent = onEvent,
                )
            },
            drawerState = drawerState,
        ) {
            OverloadAppContent(
                navigationType = navigationType,
                navigationContentPosition = navigationContentPosition,
                navController = navController,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                onDrawerClicked = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                state = state,
                onEvent = onEvent,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun OverloadAppContent(
    modifier: Modifier = Modifier,
    navigationType: OverloadNavigationType,
    navigationContentPosition: OverloadNavigationContentPosition,
    navController: NavHostController,
    selectedDestination: String,
    navigateToTopLevelDestination: (OverloadTopLevelDestination) -> Unit,
    onDrawerClicked: () -> Unit = {},
    state: ItemState,
    onEvent: (ItemEvent) -> Unit,
) {
    Row(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(visible = navigationType == OverloadNavigationType.NAVIGATION_RAIL) {
            OverloadNavigationRail(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigateToTopLevelDestination,
                onDrawerClicked = onDrawerClicked,
                state = state,
                onEvent = onEvent,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        ) {
            OverloadNavHost(
                navigationType = navigationType,
                navController = navController,
                state = state,
                onEvent = onEvent,
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (navigationType == OverloadNavigationType.BOTTOM_NAVIGATION && !state.isDeleting) {
                            Modifier.consumeWindowInsets(
                                WindowInsets.systemBars.only(
                                    WindowInsetsSides.Bottom,
                                ),
                            )
                        } else {
                            Modifier
                        },
                    ),
            )
            AnimatedVisibility(visible = navigationType == OverloadNavigationType.BOTTOM_NAVIGATION) {
                OverloadBottomNavigationBar(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination,
                    state = state,
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun OverloadNavHost(
    navigationType: OverloadNavigationType,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    state: ItemState,
    onEvent: (ItemEvent) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = OverloadRoute.HOME,
    ) {
        composable(OverloadRoute.HOME) {
            HomeTab(navigationType = navigationType, state = state, onEvent = onEvent)
        }
        composable(OverloadRoute.CALENDAR) {
            CalendarTab(state = state, onEvent = onEvent)
        }
        composable(OverloadRoute.CONFIGURATIONS) {
            ConfigurationsTab()
        }
    }
}
