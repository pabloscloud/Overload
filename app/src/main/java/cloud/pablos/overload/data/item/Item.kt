package cloud.pablos.overload.data.item

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val startTime: String,
    val endTime: String,
    val ongoing: Boolean,
    val pause: Boolean,
)
