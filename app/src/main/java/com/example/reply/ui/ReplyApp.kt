/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.reply.ui

import HomeTab
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.reply.ui.navigation.ModalNavigationDrawerContent
import com.example.reply.ui.navigation.PermanentNavigationDrawerContent
import com.example.reply.ui.navigation.ReplyBottomNavigationBar
import com.example.reply.ui.navigation.ReplyNavigationActions
import com.example.reply.ui.navigation.ReplyNavigationRail
import com.example.reply.ui.navigation.ReplyRoute
import com.example.reply.ui.navigation.ReplyTopLevelDestination
import com.example.reply.ui.utils.DevicePosture
import com.example.reply.ui.utils.ReplyNavigationContentPosition
import com.example.reply.ui.utils.ReplyNavigationType
import com.example.reply.ui.utils.isBookPosture
import com.example.reply.ui.utils.isSeparating
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyApp(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
) {
    /**
     * This will help us select type of navigation and content type depending on window size and
     * fold state of the device.
     */
    val navigationType: ReplyNavigationType

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
            navigationType = ReplyNavigationType.BOTTOM_NAVIGATION
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = ReplyNavigationType.NAVIGATION_RAIL
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
                ReplyNavigationType.NAVIGATION_RAIL
            } else {
                ReplyNavigationType.PERMANENT_NAVIGATION_DRAWER
            }
        }
        else -> {
            navigationType = ReplyNavigationType.BOTTOM_NAVIGATION
        }
    }

    /**
     * Content inside Navigation Rail/Drawer can also be positioned at top, bottom or center for
     * ergonomics and reachability depending upon the height of the device.
     */
    val navigationContentPosition = when (windowSize.heightSizeClass) {
        WindowHeightSizeClass.Compact -> {
            ReplyNavigationContentPosition.TOP
        }
        WindowHeightSizeClass.Medium,
        WindowHeightSizeClass.Expanded -> {
            ReplyNavigationContentPosition.TOP
        }
        else -> {
            ReplyNavigationContentPosition.TOP
        }
    }

    ReplyNavigationWrapper(
        navigationType = navigationType,
        navigationContentPosition = navigationContentPosition,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReplyNavigationWrapper(
    navigationType: ReplyNavigationType,
    navigationContentPosition: ReplyNavigationContentPosition,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        ReplyNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination =
        navBackStackEntry?.destination?.route ?: ReplyRoute.HOME

    if (navigationType == ReplyNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        // TODO check on custom width of PermanentNavigationDrawer: b/232495216
        PermanentNavigationDrawer(drawerContent = {
            PermanentNavigationDrawerContent(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigationActions::navigateTo,
            )
        }) {
            ReplyAppContent(
                navigationType = navigationType,
                navigationContentPosition = navigationContentPosition,
                navController = navController,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo,
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
                    }
                )
            },
            drawerState = drawerState
        ) {
            ReplyAppContent(
                navigationType = navigationType,
                navigationContentPosition = navigationContentPosition,
                navController = navController,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo,
            ) {
                scope.launch {
                    drawerState.open()
                }
            }
        }
    }
}

@Composable
fun ReplyAppContent(
    modifier: Modifier = Modifier,
    navigationType: ReplyNavigationType,
    navigationContentPosition: ReplyNavigationContentPosition,
    navController: NavHostController,
    selectedDestination: String,
    navigateToTopLevelDestination: (ReplyTopLevelDestination) -> Unit,
    onDrawerClicked: () -> Unit = {}
) {
    Row(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(visible = navigationType == ReplyNavigationType.NAVIGATION_RAIL) {
            ReplyNavigationRail(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigateToTopLevelDestination,
                onDrawerClicked = onDrawerClicked,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            ReplyNavHost(
                navController = navController,
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if(navigationType == ReplyNavigationType.BOTTOM_NAVIGATION) {
                            Modifier.consumeWindowInsets(WindowInsets.systemBars.only(
                                WindowInsetsSides.Bottom))
                        } else {
                            Modifier
                        }
                    ),
            )
            AnimatedVisibility(visible = navigationType == ReplyNavigationType.BOTTOM_NAVIGATION) {
                ReplyBottomNavigationBar(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination
                )
            }
        }
    }
}

@Composable
private fun ReplyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ReplyRoute.HOME,
    ) {
        composable(ReplyRoute.HOME) {
            HomeTab()
        }
        composable(ReplyRoute.CALENDAR) {
            CalendarTab()
        }
        composable(ReplyRoute.CONFIGURATIONS) {
            ConfigurationsTab()
        }
    }
}