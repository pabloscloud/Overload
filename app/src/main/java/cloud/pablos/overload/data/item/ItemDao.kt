package cloud.pablos.overload.data.item

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import cloud.pablos.overload.ui.views.extractDate
import cloud.pablos.overload.ui.views.parseToLocalDateTime
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

@Dao
interface ItemDao {
    @Upsert
    suspend fun upsertItem(item: Item)

    @Upsert
    suspend fun upsertItems(items: List<Item>)

    @Delete
    suspend fun deleteItem(item: Item)

    @Delete
    suspend fun deleteItems(items: List<Item>)

    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<Item>>
}

// Export function
fun backupItemsToCsv(state: ItemState): String {
    val items = state.items
    /*val gson = Gson()

    return try {
        val json = gson.toJson(items)
        json
    } catch (e: Exception) {
        "{}" // Return a placeholder JSON object
    }*/

    val csvHeader = "id,startTime,endTime,ongoing,pause\n"
    val csvData = items.joinToString("\n") { item ->
        "${item.id},${item.startTime},${item.endTime},${item.ongoing},${item.pause}"
    }

    return csvHeader + csvData
}

fun startOrStopPause(state: ItemState, onEvent: (ItemEvent) -> Unit) {
    val date = LocalDate.now()

    val itemsForToday = state.items.filter { item ->
        val startTime = parseToLocalDateTime(item.startTime)
        extractDate(startTime) == date
    }
    val isFirstToday = itemsForToday.isEmpty()
    val isOngoingToday = itemsForToday.isNotEmpty() && itemsForToday.last().ongoing

    val itemsNotToday = state.items.filter { item ->
        val startTime = parseToLocalDateTime(item.startTime)
        extractDate(startTime) != date
    }
    val isOngoingNotToday = itemsNotToday.isNotEmpty() && itemsNotToday.any { it.ongoing }

    if (isOngoingNotToday) {
        onEvent(ItemEvent.SetForgotToStopDialogShown(true))
    } else if (isFirstToday) {
        onEvent(ItemEvent.SetStart(start = LocalDateTime.now().toString()))
        onEvent(ItemEvent.SetOngoing(ongoing = true))
        onEvent(ItemEvent.SetPause(pause = false))
        onEvent(ItemEvent.SaveItem)

        onEvent(ItemEvent.SetIsOngoing(isOngoing = true))
    } else if (isOngoingToday) {
        onEvent(ItemEvent.SetId(id = itemsForToday.last().id))
        onEvent(ItemEvent.SetStart(start = itemsForToday.last().startTime))
        onEvent(ItemEvent.SetEnd(end = LocalDateTime.now().toString()))
        onEvent(ItemEvent.SetOngoing(ongoing = false))
        onEvent(ItemEvent.SaveItem)

        onEvent(ItemEvent.SetIsOngoing(isOngoing = false))
    } else {
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
    }
}
