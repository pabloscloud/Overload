package cloud.pablos.overload.ui.tabs.configurations

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Archive
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Copyright
import androidx.compose.material.icons.rounded.EmojiNature
import androidx.compose.material.icons.rounded.PestControl
import androidx.compose.material.icons.rounded.Unarchive
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.room.withTransaction
import cloud.pablos.overload.R
import cloud.pablos.overload.data.item.Item
import cloud.pablos.overload.data.item.ItemDatabase
import cloud.pablos.overload.data.item.ItemState
import cloud.pablos.overload.data.item.backupItemsToCsv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConfigurationsTab(state: ItemState) {
    val foregroundColor = MaterialTheme.colorScheme.onBackground

    val context = LocalContext.current

    val acraEnabledKey = "acra.enable"
    val acraSysLogsEnabledKey = "acra.syslog.enable"

    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val acraEnabled = sharedPreferences.getBoolean(acraEnabledKey, true)
    val acraSysLogsEnabled = sharedPreferences.getBoolean(acraSysLogsEnabledKey, true)

    val acraEnabledState = remember(acraEnabled) {
        mutableStateOf(sharedPreferences.getBoolean(acraEnabledKey, true))
    }

    val acraSysLogsEnabledState = remember(acraSysLogsEnabled) {
        mutableStateOf(sharedPreferences.getBoolean(acraSysLogsEnabledKey, true))
    }

    val importDialogState = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ConfigurationsTabTopAppBar()
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Analytics Title
            item {
                ConfigurationsTabItem(title = stringResource(id = R.string.analytics))
            }

            // Analytics Crash Reports
            item {
                ConfigurationsTabItem(
                    title = stringResource(id = R.string.crash_reports),
                    description = stringResource(id = R.string.crash_reports_descr),
                    sharedPreferences = sharedPreferences,
                    preferenceKey = acraEnabledKey,
                    state = acraEnabledState,
                    icon = Icons.Rounded.BugReport,
                )
            }

            // Analytics System Logs
            item {
                AnimatedVisibility(
                    visible = acraEnabledState.value,
                    enter = expandIn(),
                    exit = shrinkOut(),
                ) {
                    ConfigurationsTabItem(
                        title = stringResource(id = R.string.system_logs),
                        description = stringResource(id = R.string.system_logs_descr),
                        sharedPreferences = sharedPreferences,
                        preferenceKey = acraSysLogsEnabledKey,
                        state = acraSysLogsEnabledState,
                        icon = Icons.Rounded.PestControl,
                    )
                }
            }

            // Analytics Divider
            item {
                Row {
                    HorizontalDivider()
                }
            }

            // Storage Title
            item {
                ConfigurationsTabItem(title = stringResource(id = R.string.storage))
            }

            // Storage Backup
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        exportAndShare(state, context)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Archive,
                        contentDescription = stringResource(id = R.string.backup),
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
                                text = stringResource(id = R.string.backup),
                                style = TextStyle(
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                    fontWeight = FontWeight.Normal,
                                ),
                                color = foregroundColor,
                            )
                            Text(
                                text = stringResource(id = R.string.backup_descr),
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

            // Storage Import
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        importDialogState.value = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Unarchive,
                        contentDescription = stringResource(id = R.string.import_ol),
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
                                text = stringResource(id = R.string.import_ol),
                                style = TextStyle(
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                    fontWeight = FontWeight.Normal,
                                ),
                                color = foregroundColor,
                            )
                            Text(
                                text = stringResource(id = R.string.import_descr),
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

            // Storage Divider
            item {
                Row {
                    HorizontalDivider()
                }
            }

            // About Title
            item {
                ConfigurationsTabItem(title = stringResource(id = R.string.about))
            }

            // About Source Code
            item {
                ConfigurationsTabItem(
                    title = stringResource(id = R.string.sourcecode),
                    description = stringResource(id = R.string.sourcecode_descr),
                    link = "https://codeberg.org/pabloscloud/Overload".toUri(),
                    icon = Icons.Rounded.Code,
                )
            }

            // About ITS
            item {
                ConfigurationsTabItem(
                    title = stringResource(id = R.string.issue_reports),
                    description = stringResource(id = R.string.issue_reports_descr),
                    link = "https://codeberg.org/pabloscloud/Overload/issues".toUri(),
                    icon = Icons.Rounded.EmojiNature,
                )
            }

            // About License
            item {
                ConfigurationsTabItem(
                    title = stringResource(id = R.string.license),
                    description = stringResource(id = R.string.license_descr),
                    link = "https://codeberg.org/pabloscloud/Overload/raw/branch/main/LICENSE".toUri(),
                    icon = Icons.Rounded.Copyright,
                )
            }

            // About Divider
            item {
                Row {
                    HorizontalDivider()
                }
            }

            // Footer
            item {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.footer),
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.labelLarge.fontSize,
                            fontWeight = FontWeight.Normal,
                        ),
                        color = foregroundColor,
                    )
                }
            }
        }
        if (importDialogState.value) {
            ConfigurationsTabImportDialog(onClose = { importDialogState.value = false })
        }
    }
}

fun exportAndShare(state: ItemState, context: Context) {
    try {
        val exportedData = backupItemsToCsv(state)
        val cachePath = File(context.cacheDir, "backup.csv")

        cachePath.writeText(exportedData)

        val contentUri = FileProvider.getUriForFile(
            context,
            "cloud.pablos.overload.fileprovider",
            cachePath,
        )

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, contentUri)
            type = "text/comma-separated-values"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    } catch (e: Exception) {
        Toast.makeText(context, "Backup and share failed", Toast.LENGTH_SHORT).show()
        e.printStackTrace()
    }
}

fun handleIntent(intent: Intent?, lifecycleScope: LifecycleCoroutineScope, db: ItemDatabase, context: Context, contentResolver: ContentResolver) {
    if (intent != null && intent.action == Intent.ACTION_SEND && intent.type == "text/comma-separated-values") {
        if (intent.getStringExtra(Intent.EXTRA_TEXT)?.isBlank() == false) {
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
            if (sharedText != null) {
                importCsvData(
                    sharedText,
                    lifecycleScope,
                    db,
                    context,
                )
            }
        } else if (intent.getStringExtra(Intent.EXTRA_STREAM)?.isBlank() == false) {
            val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
            uri?.let {
                importFile(uri, contentResolver, context, db, lifecycleScope)
            }
        } else if (intent.clipData != null) {
            val uri = intent.clipData?.getItemAt(0)?.uri
            if (uri != null) {
                importFile(uri, contentResolver, context, db, lifecycleScope)
            } else {
                showImportFailedToast(context)
            }
        } else {
            showImportFailedToast(context)
        }
    }
}

private fun importCsvData(csvData: String, lifecycleScope: LifecycleCoroutineScope, db: ItemDatabase, context: Context) {
    val parsedData = parseCsvData(csvData)

    lifecycleScope.launch(Dispatchers.IO) {
        val itemDao = db.itemDao()

        var allImportsSucceeded = true

        db.withTransaction {
            parsedData.drop(1).forEach { row ->
                if (row.size >= 4) {
                    val startTime = row[1]
                    val endTime = row[2]
                    val ongoing = row[3]
                    val pause = row[4]

                    val item = Item(
                        startTime = startTime,
                        endTime = endTime,
                        ongoing = ongoing.toBoolean(),
                        pause = pause.toBoolean(),
                    )

                    val importResult = itemDao.upsertItem(item)
                    if (importResult != Unit) {
                        allImportsSucceeded = false
                    }
                }
            }
        }

        withContext(Dispatchers.Main) {
            if (allImportsSucceeded) {
                showImportSuccessToast(context)
            } else {
                showImportFailedToast(context)
            }
        }
    }
}

fun parseCsvData(csvData: String): List<List<String>> {
    val rows = csvData.split("\n")
    return rows.map { row ->
        val separator = when {
            row.contains(',') -> ","
            row.contains(';') -> ";"
            else -> ","
        }

        row.split(separator)
    }
}

fun showImportSuccessToast(context: Context) {
    Toast.makeText(context, context.getString(R.string.import_success), Toast.LENGTH_SHORT).show()
}

fun showImportFailedToast(context: Context) {
    Toast.makeText(context, context.getString(R.string.import_failure), Toast.LENGTH_SHORT).show()
}

private fun importFile(uri: Uri, contentResolver: ContentResolver, context: Context, db: ItemDatabase, lifecycleScope: LifecycleCoroutineScope) {
    uri.let { uri ->
        val contentResolver = contentResolver
        contentResolver.openInputStream(uri)?.use { inputStream ->
            val sharedData = inputStream.bufferedReader().readText()

            importCsvData(sharedData, lifecycleScope, db, context)
        }
    }
}

/*
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ConfigurationsTabPreview() {
    ConfigurationsTab()
}*/
