package cloud.pablos.overload.data.item

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
                    for (item in event.items) {
                        dao.deleteItem(item)
                    }
                }

                _state.update { it.copy(selectedItems = emptyList()) }
                _state.update { it.copy(isDeleting = false) }
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

            is ItemEvent.SetSelectedDay -> {
                _state.update { it.copy(selectedDay = event.day) }
            }

            is ItemEvent.SetIsDeleting -> {
                _state.update { it.copy(isDeleting = event.isDeleting) }

                when (event.isDeleting) {
                    false -> _state.update { it.copy(selectedItems = emptyList()) }
                    else -> {}
                }
            }

            is ItemEvent.SetSelectedItems -> {
                _state.update { it.copy(selectedItems = event.selectedItems) }
            }
        }
    }
}
