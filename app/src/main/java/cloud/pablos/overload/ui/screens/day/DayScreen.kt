package cloud.pablos.overload.ui.screens.day

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cloud.pablos.overload.data.item.ItemEvent
import cloud.pablos.overload.data.item.ItemState
import cloud.pablos.overload.ui.navigation.OverloadRoute
import cloud.pablos.overload.ui.navigation.OverloadTopAppBar
import cloud.pablos.overload.ui.views.DayView
import cloud.pablos.overload.ui.views.getLocalDate

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun DayScreen(
    state: ItemState,
    onEvent: (ItemEvent) -> Unit,
) {
    val selectedDay = getLocalDate(state.selectedDayCalendar)

    Scaffold(
        topBar = {
            OverloadTopAppBar(
                selectedDestination = OverloadRoute.DAY,
                state = state,
                onEvent = onEvent,
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(paddingValues)) {
                DayView(
                    state = state,
                    onEvent = onEvent,
                    date = selectedDay,
                    isEditable = true,
                )
            }
        }
    }
}
