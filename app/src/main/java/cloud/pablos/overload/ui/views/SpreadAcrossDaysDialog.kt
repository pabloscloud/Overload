package cloud.pablos.overload.ui.views

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import cloud.pablos.overload.R
import cloud.pablos.overload.data.item.Item
import cloud.pablos.overload.data.item.ItemEvent
import cloud.pablos.overload.data.item.ItemState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SpreadAcrossDaysDialog(
    onClose: () -> Unit,
    state: ItemState,
    onEvent: (ItemEvent) -> Unit,
) {
    val context = LocalContext.current
    val learnMoreLink = "https://codeberg.org/pabloscloud/Overload#spread-acorss-days".toUri()

    val date = LocalDate.now()

    val itemsNotToday = state.items.filter { item ->
        val startTime = parseToLocalDateTime(item.startTime)
        extractDate(startTime) != date
    }
    val isOngoingNotToday = itemsNotToday.isNotEmpty() && itemsNotToday.any { it.ongoing }
    val firstOngoingItem = itemsNotToday.find { it.ongoing }

    if (isOngoingNotToday && firstOngoingItem != null) {
        AlertDialog(
            onDismissRequest = { onClose() },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = stringResource(R.string.spread_across_days),
                    tint = MaterialTheme.colorScheme.error,
                )
            },
            title = {
                TextView(
                    text = stringResource(R.string.spread_across_days),
                    fontWeight = FontWeight.Bold,
                    align = TextAlign.Center,
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            text = {
                Column {
                    Text(
                        text = stringResource(R.string.are_you_sure),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val openLinkStr = stringResource(id = R.string.open_link_with)
                    TextView(
                        text = stringResource(id = R.string.learn_more),
                        color = MaterialTheme.colorScheme.primary,
                        align = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, learnMoreLink)
                                val chooserIntent = Intent.createChooser(intent, openLinkStr)
                                ContextCompat.startActivity(context, chooserIntent, null)
                            },
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    DayViewItemOngoing(item = firstOngoingItem, showDate = true, hideEnd = true)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onClose()
                    },
                ) {
                    TextView(stringResource(R.string.no))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        saveAndClose(onClose = onClose, onEvent, firstOngoingItem)
                    },
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                ) {
                    TextView(stringResource(R.string.yes))
                }
            },
            modifier = Modifier.padding(16.dp),
        )
    } else {
        onClose()
    }
}

private fun saveAndClose(
    onClose: () -> Unit,
    onEvent: (ItemEvent) -> Unit,
    item: Item,
) {
    val currentDate = LocalDate.now()

    val startTime = parseToLocalDateTime(item.startTime)
    val startDate = startTime.toLocalDate()
    var dateIterator = startTime.toLocalDate()

    while (dateIterator.isBefore(currentDate) || dateIterator == currentDate) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val newStartTime = if (dateIterator == startDate) startTime else LocalDateTime.of(dateIterator, LocalTime.MIDNIGHT)
        val newEndTime = if (dateIterator == currentDate) parseToLocalDateTime(LocalDateTime.now().toString()) else LocalDateTime.of(dateIterator, LocalTime.MAX)

        if (dateIterator == startDate) {
            onEvent(ItemEvent.SetId(id = item.id))
        }
        onEvent(ItemEvent.SetStart(start = newStartTime.format(formatter)))
        onEvent(ItemEvent.SetEnd(end = newEndTime.format(formatter)))
        onEvent(ItemEvent.SetOngoing(ongoing = false))
        onEvent(ItemEvent.SetPause(pause = item.pause))
        onEvent(ItemEvent.SaveItem)
        dateIterator = dateIterator.plusDays(1)
    }

    onClose()
}

/*@Preview
@Composable
fun SpreadAcrossDaysDialogPreview() {
    SpreadAcrossDaysDialog {}
}*/