package cloud.pablos.overload.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import cloud.pablos.overload.R
import cloud.pablos.overload.data.item.ItemEvent
import cloud.pablos.overload.data.item.ItemState
import cloud.pablos.overload.ui.utils.OverloadNavigationContentPosition
import cloud.pablos.overload.ui.views.TextView
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun OverloadNavigationRail(
    selectedDestination: String,
    navigationContentPosition: OverloadNavigationContentPosition,
    navigateToTopLevelDestination: (OverloadTopLevelDestination) -> Unit,
    onDrawerClicked: () -> Unit = {},
    state: ItemState,
    onEvent: (ItemEvent) -> Unit,
) {
    if (!state.isDeletingHome) {
        NavigationRail(
            modifier = Modifier.fillMaxHeight(),
            containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        ) {
            Layout(
                modifier = Modifier.widthIn(max = 80.dp),
                content = {
                    Column(
                        modifier = Modifier.layoutId(LayoutType.HEADER),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        NavigationRailItem(
                            selected = false,
                            onClick = onDrawerClicked,
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = stringResource(id = R.string.navigation_drawer),
                                )
                            },
                        )

                        AnimatedVisibility(
                            visible = state.selectedDayCalendar == LocalDate.now().toString(),
                        ) {
                            OverloadNavigationFabSmall(state = state, onEvent = onEvent)
                        }
                    }
                    Column(modifier = Modifier.layoutId(LayoutType.CONTENT)) {
                        AnimatedVisibility(visible = state.isFabOpen.not()) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                TOP_LEVEL_DESTINATIONS.forEach { overloadDestination ->
                                    NavigationRailItem(
                                        selected = selectedDestination == overloadDestination.route,
                                        onClick = { navigateToTopLevelDestination(overloadDestination) },
                                        icon = {
                                            Icon(
                                                imageVector =
                                                if (selectedDestination == overloadDestination.route) {
                                                    overloadDestination.selectedIcon
                                                } else {
                                                    overloadDestination.unselectedIcon
                                                },
                                                contentDescription = stringResource(id = overloadDestination.iconTextId),
                                            )
                                        },
                                    )
                                }
                            }
                        }
                    }
                },
                measurePolicy = navigationMeasurePolicy(navigationContentPosition),
            )
        }
    }
}

@Composable
fun OverloadBottomNavigationBar(
    selectedDestination: String,
    navigateToTopLevelDestination: (OverloadTopLevelDestination) -> Unit,
    state: ItemState,
) {
    if (!state.isDeletingHome) {
        NavigationBar(modifier = Modifier.fillMaxWidth()) {
            TOP_LEVEL_DESTINATIONS.forEach { overloadDestination ->
                NavigationBarItem(
                    selected = selectedDestination == overloadDestination.route,
                    onClick = { navigateToTopLevelDestination(overloadDestination) },
                    icon = {
                        Icon(
                            imageVector =
                            if (selectedDestination == overloadDestination.route) {
                                overloadDestination.selectedIcon
                            } else {
                                overloadDestination.unselectedIcon
                            },
                            contentDescription = stringResource(id = overloadDestination.iconTextId),
                        )
                    },
                    label = {
                        val label = when (overloadDestination.route) {
                            "Home" -> { stringResource(id = R.string.home) }
                            "Calendar" -> { stringResource(id = R.string.calendar) }
                            "Configurations" -> { stringResource(id = R.string.configurations) }
                            else -> { stringResource(id = R.string.unknown_day) }
                        }
                        TextView(
                            text = label,
                            fontSize = MaterialTheme.typography.labelLarge.fontSize,
                            fontWeight =
                            if (selectedDestination == overloadDestination.route) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            },
                        )
                    },
                )
            }
        }
    }
}

@Composable
fun ModalNavigationDrawerContent(
    selectedDestination: String,
    navigationContentPosition: OverloadNavigationContentPosition,
    navigateToTopLevelDestination: (OverloadTopLevelDestination) -> Unit,
    onDrawerClicked: () -> Unit = {},
    state: ItemState,
    onEvent: (ItemEvent) -> Unit,
) {
    if (!state.isDeletingHome) {
        ModalDrawerSheet {
            Layout(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
                    .padding(16.dp),
                content = {
                    Column(
                        modifier = Modifier.layoutId(LayoutType.HEADER),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            TextView(
                                text = stringResource(id = R.string.app_name),
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            )
                            IconButton(onClick = onDrawerClicked) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.MenuOpen,
                                    contentDescription = stringResource(id = R.string.navigation_drawer),
                                )
                            }
                        }

                        AnimatedVisibility(visible = state.selectedDayCalendar == LocalDate.now().toString()) {
                            OverloadNavigationFab(state, onEvent, onDrawerClicked)
                        }
                    }

                    Column(modifier = Modifier.layoutId(LayoutType.CONTENT)) {
                        AnimatedVisibility(visible = state.isFabOpen.not()) {
                            Column(
                                modifier = Modifier.verticalScroll(rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                TOP_LEVEL_DESTINATIONS.forEach { overloadDestination ->
                                    NavigationDrawerItem(
                                        selected = selectedDestination == overloadDestination.route,
                                        label = {
                                            TextView(stringResource(id = overloadDestination.iconTextId))
                                        },
                                        icon = {
                                            Icon(
                                                imageVector =
                                                if (selectedDestination == overloadDestination.route) {
                                                    overloadDestination.selectedIcon
                                                } else {
                                                    overloadDestination.unselectedIcon
                                                },
                                                contentDescription = stringResource(
                                                    id = overloadDestination.iconTextId,
                                                ),
                                            )
                                        },
                                        colors = NavigationDrawerItemDefaults.colors(
                                            unselectedContainerColor = Color.Transparent,
                                        ),
                                        onClick = { navigateToTopLevelDestination(overloadDestination) },
                                    )
                                }
                            }
                        }
                    }
                },
                measurePolicy = navigationMeasurePolicy(navigationContentPosition),
            )
        }
    }
}

fun navigationMeasurePolicy(
    navigationContentPosition: OverloadNavigationContentPosition,
): MeasurePolicy {
    return MeasurePolicy { measurables, constraints ->
        lateinit var headerMeasurable: Measurable
        lateinit var contentMeasurable: Measurable
        measurables.forEach {
            when (it.layoutId) {
                LayoutType.HEADER -> headerMeasurable = it
                LayoutType.CONTENT -> contentMeasurable = it
                else -> error("Unknown layoutId encountered!")
            }
        }

        val headerPlaceable = headerMeasurable.measure(constraints)
        val contentPlaceable = contentMeasurable.measure(
            constraints.offset(vertical = -headerPlaceable.height),
        )
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Place the header, this goes at the top
            headerPlaceable.placeRelative(0, 0)

            // Determine how much space is not taken up by the content
            val nonContentVerticalSpace = constraints.maxHeight - contentPlaceable.height

            val contentPlaceableY = when (navigationContentPosition) {
                // Figure out the place we want to place the content, with respect to the
                // parent (ignoring the header for now)
                OverloadNavigationContentPosition.TOP -> 0
                OverloadNavigationContentPosition.CENTER -> nonContentVerticalSpace / 2
            }
                // And finally, make sure we don't overlap with the header.
                .coerceAtLeast(headerPlaceable.height)

            contentPlaceable.placeRelative(0, contentPlaceableY)
        }
    }
}

enum class LayoutType {
    HEADER, CONTENT
}
