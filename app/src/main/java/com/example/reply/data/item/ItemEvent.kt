package com.example.reply.data.item

sealed interface ItemEvent {
    object SaveItem: ItemEvent
    data class SetId(val id: Int): ItemEvent
    data class SetStart(val start: String): ItemEvent
    data class SetEnd(val end: String): ItemEvent
    data class SetOngoing(val ongoing: Boolean): ItemEvent
    data class SetPause(val pause: Boolean): ItemEvent
    data class SetIsOngoing(val isOngoing: Boolean): ItemEvent
    data class DeleteItem(val item: Item): ItemEvent
    data class SetSelectedDay(val day: String): ItemEvent
}