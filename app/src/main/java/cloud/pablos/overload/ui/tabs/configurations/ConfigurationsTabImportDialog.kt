package cloud.pablos.overload.ui.tabs.configurations

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import cloud.pablos.overload.R

@Composable
fun ConfigurationsTabImportDialog(onClose: () -> Unit) {
    val context = LocalContext.current
    val learnMoreLink = "https://codeberg.org/pabloscloud/Overload#import-backup".toUri()

    AlertDialog(
        onDismissRequest = { onClose() },
        icon = {
            Icon(
                imageVector = Icons.Rounded.Info,
                contentDescription = stringResource(id = R.string.import_backup),
                tint = MaterialTheme.colorScheme.primary,
            )
        },
        title = {
            Text(
                text = stringResource(id = R.string.import_backup),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        text = {
            Column {
                Text(
                    text = stringResource(id = R.string.import_backup_descr),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Learn more...",
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, learnMoreLink)
                            val chooserIntent = Intent.createChooser(intent, "Open link with")
                            ContextCompat.startActivity(context, chooserIntent, null)
                        },
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onClose() },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Close")
            }
        },
        modifier = Modifier.padding(16.dp),
    )
}

@Preview
@Composable
fun ConfigurationsTabImportDialogPreview() {
    ConfigurationsTabImportDialog {}
}
