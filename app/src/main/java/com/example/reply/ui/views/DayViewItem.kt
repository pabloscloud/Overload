/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.reply.ui.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ModeNight
import androidx.compose.material.icons.filled.NightShelter
import androidx.compose.material.icons.filled.Nightlife
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.reply.data.Item
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayViewItem(item: Item){
    val backgroundColor =
        if (item.pause) MaterialTheme.colorScheme.onSecondaryContainer
        else MaterialTheme.colorScheme.secondaryContainer

    val foregroundColor =
        if (item.pause) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.onSecondaryContainer


    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val startTimeString = item.startTime.format(timeFormatter)
    val endTimeString = item.endTime.format(timeFormatter)

    val duration = Duration.between(item.startTime, item.endTime)
    val hours = duration.toHours()
    val minutes = duration.minusHours(hours).toMinutes()
    val durationString = when {
        hours > 0 -> "${hours}h ${minutes}min"
        else -> "${minutes}min"
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(backgroundColor)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = startTimeString,
                maxLines = 1,
                color = foregroundColor,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(12.5.dp)
            )

            if (item.pause)
                Icon(
                    Icons.Outlined.DarkMode,
                    contentDescription = "Pause",
                    tint = foregroundColor
                )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
                    .offset(x = 5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp)
                            .background(foregroundColor)
                    )
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = foregroundColor,
                        modifier = Modifier.offset(x = (-5).dp).size(25.dp)
                    )
                }
            }

            Text(
                text = endTimeString,
                maxLines = 1,
                color = foregroundColor,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(12.5.dp)
            )
        }

        // Text to be added on top of the item "2h 7min"
        Text(
            text = durationString,
            color = foregroundColor, // Customize the color as needed
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .background(backgroundColor)
                .padding(8.dp)
        )
    }




}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DayViewItemPreview(){
    val exampleItem = Item(
        startTime = LocalDateTime.of(2023, 7, 29, 11, 57),
        endTime = LocalDateTime.of(2023, 7, 29, 12, 31),
        pause = true
    )

    DayViewItem(exampleItem)
}