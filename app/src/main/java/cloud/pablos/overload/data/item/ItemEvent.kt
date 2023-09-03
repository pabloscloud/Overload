package cloud.pablos.overload.data.item

sealed interface ItemEvent {
    object SaveItem : ItemEvent
    data class SetId(val id: Int) : ItemEvent
    data class SetStart(val start: String) : ItemEvent
    data class SetEnd(val end: String) : ItemEvent
    data class SetOngoing(val ongoing: Boolean) : ItemEvent
    data class SetPause(val pause: Boolean) : ItemEvent
    data class SetIsOngoing(val isOngoing: Boolean) : ItemEvent
    data class DeleteItems(val items: List<Item>) : ItemEvent
    data class SetSelectedDay(val day: String) : ItemEvent
    data class SetSelectedYear(val year: Int) : ItemEvent
    data class SetIsDeleting(val isDeleting: Boolean) : ItemEvent
    data class SetIsSelected(val isSelected: Boolean) : ItemEvent
    data class SetSelectedItems(val selectedItems: List<Item>) : ItemEvent
}
