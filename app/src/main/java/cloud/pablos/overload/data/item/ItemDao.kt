package cloud.pablos.overload.data.item

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

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
