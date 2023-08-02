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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BlinkingClock(foregroundColor: Color) {
    var showDots by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(showDots) {
        while (true) {
            delay(500) // Blink every 500ms
            showDots = !showDots
        }
    }
    val currentTime = LocalTime.now()
    val hours = currentTime.format(DateTimeFormatter.ofPattern("HH"))
    val minutes = currentTime.format(DateTimeFormatter.ofPattern("mm"))

    ClockText(hours, minutes, showDots, foregroundColor)
}

@Composable
fun ClockText(hours: String, minutes: String, showDots: Boolean, foregroundColor: Color) {
    Text(
        text = if (showDots) "$hours:$minutes" else "$hours $minutes",
        maxLines = 1,
        color = foregroundColor,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(12.5.dp)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun BlinkingClockPreview() {
    BlinkingClock(MaterialTheme.colorScheme.onSecondaryContainer)
}
