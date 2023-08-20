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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConfigurationsTabItem(
    title: String,
) {
    var foregroundColor = MaterialTheme.colorScheme.onBackground

    Row(Modifier.padding(top = 16.dp)) {
        Text(
            text = title.replaceFirstChar { it.uppercase() },
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.Normal,
            ),
            color = foregroundColor,
        )
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
    var foregroundColor = MaterialTheme.colorScheme.onBackground

    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            val intent = Intent(Intent.ACTION_VIEW, link)
            val chooserIntent = Intent.createChooser(intent, "Open link with")
            startActivity(context, chooserIntent, null)
        },
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.Normal,
                    ),
                    color = foregroundColor,
                )
                Text(
                    text = description,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = FontWeight.Normal,
                    ),
                    color = foregroundColor,
                )
            }
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
    var foregroundColor = MaterialTheme.colorScheme.onBackground
    val acraSysLogsEnabledKey = "acra.syslog.enable"

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
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.Normal,
                    ),
                    color = foregroundColor,
                )
                Text(
                    text = description,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = FontWeight.Normal,
                    ),
                    color = foregroundColor,
                )
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
