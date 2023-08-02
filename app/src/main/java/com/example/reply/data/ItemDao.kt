package com.example.reply.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Upsert
    suspend fun upsertItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)

    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<Item>>
}
