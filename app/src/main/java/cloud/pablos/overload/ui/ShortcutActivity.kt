package cloud.pablos.overload.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import cloud.pablos.overload.data.item.ItemDatabase
import cloud.pablos.overload.data.item.ItemViewModel

class ShortcutActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updateDatabase()
        finish()

        /*setContent {
            OverloadTheme {
                val state by viewModel.state.collectAsState()
                val onEvent = viewModel::onEvent

                val context = LocalContext.current

                startOrStopPause(state, onEvent, context)
            }
        }
        finish()*/
    }

    private fun updateDatabase() {
        val db by lazy {
            Room.databaseBuilder(
                applicationContext,
                ItemDatabase::class.java,
                "items",
            ).build()
        }

        val viewModel by viewModels<ItemViewModel>(
            factoryProducer = {
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return ItemViewModel(db.itemDao()) as T
                    }
                }
            },
        )

        viewModel.shortcutPressed(this)
    }
}
