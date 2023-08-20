package cloud.pablos.overload.ui.tabs.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarViewDay
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cloud.pablos.overload.R
import cloud.pablos.overload.data.item.ItemEvent
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarTabYearDialog(
    onClose: () -> Unit,
    onEvent: (ItemEvent) -> Unit,
) {
    Dialog(
        onDismissRequest = { onClose() },
        content = {
            Surface(
                shape = MaterialTheme.shapes.large,
                tonalElevation = NavigationBarDefaults.Elevation,
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp),
                ) {
                    Box(
                        Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CalendarViewDay,
                            contentDescription = stringResource(id = R.string.select_year),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp),
                        )
                    }

                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally),
                    ) {
                        Text(
                            text = stringResource(id = R.string.select_year),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                        )
                    }

                    Box(modifier = Modifier.align(Alignment.End)) {
                        LazyColumn(
                            modifier = Modifier.fillMaxHeight(),
                        ) {
                            val currentYear = LocalDate.now().year
                            for (i in currentYear downTo currentYear - 100) {
                                item {
                                    YearRow(year = i, onEvent = onEvent, onClose = onClose)
                                    if (i != currentYear - 100) {
                                        HorizontalDivider()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
    )
}

@Composable
fun YearRow(year: Int, onEvent: (ItemEvent) -> Unit, onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onEvent(ItemEvent.SetSelectedYear(year))
                onClose()
            },
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(year.toString())
    }
}

/*
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun CalendarTabYearDialogPreview() {
    CalendarTabYearDialog {}
}*/
