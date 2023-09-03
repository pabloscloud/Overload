package cloud.pablos.overload.data.item

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class ItemState(
    val items: List<Item> = emptyList(),
    val id: Int = 0,
    val start: String = LocalDateTime.now().toString(),
    val end: String = "",
    val ongoing: Boolean = false,
    val pause: Boolean = false,
    val isOngoing: Boolean = false,
    val selectedDay: String = "",
    val selectedYear: Int = 0,
    val isDeleting: Boolean = false,
    val isSelected: Boolean = false,
    val selectedItems: List<Item> = emptyList()
)
