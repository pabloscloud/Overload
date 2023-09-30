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
    val selectedDayCalendar: String = "",
    val selectedYearCalendar: Int = 0,
    val isDeletingHome: Boolean = false,
    val isSelectedHome: Boolean = false,
    val selectedItemsHome: List<Item> = emptyList(),
)
