package cloud.pablos.overload.ui.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import cloud.pablos.overload.R
import cloud.pablos.overload.ui.ShortcutActivity

object ShortcutUtil {

    private const val stopPauseId = "stop_pause"
    private const val startPauseId = "stop_pause"
    fun addShortcutStopPause(
        context: Context,
    ) {
        val shortcut = ShortcutInfoCompat.Builder(context, stopPauseId)
            .setShortLabel("stop pause")
            .setLongLabel("stop the pause")
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_launcher_background))
            .setIntent(
                Intent(context, ShortcutActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                },
            )
            .build()
        ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
    }

    fun removeShortcutStopPause(
        context: Context,
    ) {
        val shortcuts: MutableList<String> = mutableListOf()
        shortcuts.add(stopPauseId)
        ShortcutManagerCompat.removeDynamicShortcuts(context, shortcuts)
    }

    fun createShortcutStartPause(
        context: Context,
    ) {
        val shortcut = ShortcutInfoCompat.Builder(context, startPauseId)
            .setShortLabel("start pause")
            .setLongLabel("start the pause")
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_launcher_background))
            .setIntent(
                Intent(context, ShortcutActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                },
            )
            .build()
        ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
    }

    fun removeShortcutStartPause(
        context: Context,
    ) {
        val shortcuts: MutableList<String> = mutableListOf()
        shortcuts.add(startPauseId)
        ShortcutManagerCompat.removeDynamicShortcuts(context, shortcuts)
    }
}
