package cloud.pablos.overload.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import cloud.pablos.overload.data.item.ItemDatabase
import cloud.pablos.overload.data.item.ItemViewModel
import cloud.pablos.overload.ui.tabs.home.startOrStopPause
import cloud.pablos.overload.ui.theme.OverloadTheme

class ShortcutActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            ItemDatabase::class.java,
            "items",
        ).build()
    }

    private val viewModel by viewModels<ItemViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ItemViewModel(db.itemDao()) as T
                }
            }
        },
    )

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OverloadTheme {
                val state by viewModel.state.collectAsState()
                val onEvent = viewModel::onEvent

                val context = LocalContext.current

                startOrStopPause(state, onEvent, context)
            }
        }
        finish()
    }
}
