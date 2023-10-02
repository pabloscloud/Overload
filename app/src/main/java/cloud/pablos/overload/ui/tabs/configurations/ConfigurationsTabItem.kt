package cloud.pablos.overload.ui.tabs.configurations

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import cloud.pablos.overload.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConfigurationsTabItem(
    title: String,
) {
    Row(Modifier.padding(top = 16.dp)) {
        ConfigurationLabel(title.replaceFirstChar { it.uppercase() })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConfigurationsTabItem(
    title: String,
    description: String,
    link: Uri,
    icon: ImageVector,
) {
    val context = LocalContext.current

    val openLinkStr = stringResource(id = R.string.open_link_with)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            val intent = Intent(Intent.ACTION_VIEW, link)
            val chooserIntent = Intent.createChooser(intent, openLinkStr)
            startActivity(context, chooserIntent, null)
        },
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
        Column() {
            ConfigurationTitle(title)
            ConfigurationDescription(description)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConfigurationsTabItem(
    title: String,
    description: String,
    sharedPreferences: SharedPreferences,
    preferenceKey: String,
    state: MutableState<Boolean>,
    icon: ImageVector,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                ConfigurationTitle(title)
                ConfigurationDescription(description)
            }
            AcraSwitch(
                sharedPreferences = sharedPreferences,
                preferenceKey = preferenceKey,
                state = state,
                onCheckedChange = { newChecked ->
                    sharedPreferences.edit().putBoolean(preferenceKey, newChecked).apply()
                },
            )
        }
    }
}

@Composable
fun AcraSwitch(
    sharedPreferences: SharedPreferences,
    preferenceKey: String,
    state: MutableState<Boolean>,
    onCheckedChange: (Boolean) -> Unit,
) {
    Switch(
        checked = state.value,
        onCheckedChange = { newChecked ->
            state.value = newChecked
            onCheckedChange(newChecked)
            sharedPreferences.edit().putBoolean(preferenceKey, newChecked).apply()
        },
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ConfigurationsTabItemPreview() {
    ConfigurationsTabItem(
        title = "F-Droid",
        description = "please support them",
        link = "https://f-droid.org".toUri(),
        icon = Icons.Rounded.Info,
    )
}
