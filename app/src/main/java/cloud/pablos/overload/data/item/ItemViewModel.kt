package cloud.pablos.overload.data.item

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cloud.pablos.overload.ui.tabs.home.startOrStopPause
import cloud.pablos.overload.ui.utils.ShortcutUtil
import cloud.pablos.overload.ui.views.extractDate
import cloud.pablos.overload.ui.views.parseToLocalDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
class ItemViewModel(
    private val dao: ItemDao,
) : ViewModel() {

    private val _items = dao.getAllItems().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(ItemState())
    val state = combine(_state, _items) { state, items ->
        state.copy(
            items = items,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ItemState())

    fun onEvent(event: ItemEvent) {
        when (event) {
            is ItemEvent.DeleteItems -> {
                viewModelScope.launch {
                    dao.deleteItems(event.items)

                    _state.update {
                        it.copy(
                            isDeletingHome = false,
                            selectedItemsHome = emptyList(),
                        )
                    }
                }
            }
            ItemEvent.SaveItem -> {
                val id = _state.value.id
                val start = _state.value.start
                val end = _state.value.end
                val ongoing = _state.value.ongoing
                val pause = _state.value.pause

                val item = Item(
                    id = id,
                    startTime = start,
                    endTime = end,
                    ongoing = ongoing,
                    pause = pause,
                )

                viewModelScope.launch {
                    dao.upsertItem(item)
                }

                _state.update {
                    it.copy(
                        id = 0,
                        start = LocalDateTime.now().toString(),
                        end = "",
                        ongoing = false,
                        pause = false,
                    )
                }
            }
            is ItemEvent.SetStart -> {
                _state.update {
                    it.copy(
                        start = event.start,
                    )
                }
            }
            is ItemEvent.SetEnd -> {
                _state.update {
                    it.copy(
                        end = event.end,
                    )
                }
            }
            is ItemEvent.SetOngoing -> {
                _state.update {
                    it.copy(
                        ongoing = event.ongoing,
                    )
                }
            }
            is ItemEvent.SetPause -> {
                _state.update {
                    it.copy(
                        pause = event.pause,
                    )
                }
            }
            is ItemEvent.SetIsOngoing -> {
                _state.update {
                    it.copy(
                        isOngoing = event.isOngoing,
                    )
                }
            }
            is ItemEvent.SetId -> {
                _state.update {
                    it.copy(
                        id = event.id,
                    )
                }
            }

            is ItemEvent.SetSelectedDayCalendar -> {
                _state.update { it.copy(selectedDayCalendar = event.day) }
            }

            is ItemEvent.SetSelectedYearCalendar -> {
                _state.update { it.copy(selectedYearCalendar = event.year) }
            }

            is ItemEvent.SetIsDeletingHome -> {
                _state.update { it.copy(isDeletingHome = event.isDeleting) }

                when (event.isDeleting) {
                    false -> _state.update { it.copy(selectedItemsHome = emptyList()) }
                    else -> {}
                }
            }

            is ItemEvent.SetIsSelectedHome -> {
                _state.update {
                    it.copy(
                        isSelectedHome = event.isSelected,
                    )
                }
            }

            is ItemEvent.SetSelectedItemsHome -> {
                _state.update { it.copy(selectedItemsHome = event.selectedItems) }
            }
        }
    }

    fun shortcutPressed(context: Context) {
        val state = state

        Log.d("123123", "items: $state")

        startOrStopPause(state, onEvent, context)

        /* val date = LocalDate.now()

        val itemsForToday = state.items.filter { item ->
            val startTime = parseToLocalDateTime(item.startTime)
            extractDate(startTime) == date
        }

        val isFirst = itemsForToday.isEmpty()
        val isNotOngoing = itemsForToday.isEmpty() || !state.items.last().ongoing

        if (isFirst) {
            onEvent(ItemEvent.SetStart(start = LocalDateTime.now().toString()))
            onEvent(ItemEvent.SetOngoing(ongoing = true))
            onEvent(ItemEvent.SetPause(pause = false))
            onEvent(ItemEvent.SaveItem)

            onEvent(ItemEvent.SetIsOngoing(isOngoing = true))
        } else if (isNotOngoing) {
            onEvent(ItemEvent.SetStart(start = itemsForToday.last().endTime))
            onEvent(ItemEvent.SetEnd(end = LocalDateTime.now().toString()))
            onEvent(ItemEvent.SetOngoing(ongoing = false))
            onEvent(ItemEvent.SetPause(pause = true))
            onEvent(ItemEvent.SaveItem)

            onEvent(ItemEvent.SetStart(start = LocalDateTime.now().toString()))
            onEvent(ItemEvent.SetOngoing(ongoing = true))
            onEvent(ItemEvent.SetPause(pause = false))
            onEvent(ItemEvent.SaveItem)

            onEvent(ItemEvent.SetIsOngoing(isOngoing = true))

            ShortcutUtil.removeShortcutStopPause(context)
            ShortcutUtil.createShortcutStartPause(context)
        } else {
            onEvent(ItemEvent.SetId(id = itemsForToday.last().id))
            onEvent(ItemEvent.SetStart(start = itemsForToday.last().startTime))
            onEvent(ItemEvent.SetEnd(end = LocalDateTime.now().toString()))
            onEvent(ItemEvent.SetOngoing(ongoing = false))
            onEvent(ItemEvent.SaveItem)

            onEvent(ItemEvent.SetIsOngoing(isOngoing = false))

            ShortcutUtil.removeShortcutStartPause(context)
            ShortcutUtil.addShortcutStopPause(context)
        }*/
    }
}
