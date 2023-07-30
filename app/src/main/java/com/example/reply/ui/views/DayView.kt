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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.reply.data.Item
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
fun createItems(): List<Item> {
    return listOf(
        Item(
            startTime = LocalDateTime.of(2023, 7, 29, 11, 57),
            endTime = LocalDateTime.of(2023, 7, 29, 12, 31),
            pause = true
        ),
        Item(
            startTime = LocalDateTime.of(2023, 7, 29, 12, 31), // Year, month, day, hour, minute
            endTime = LocalDateTime.of(2023, 7, 29, 14, 38),
            pause = false
        ),
        // Add more items here as needed...
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayView(){
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        val exampleItems = createItems()
        items(exampleItems.size) { index ->
            val isFirstItem = index == 0

            Box(
                modifier = Modifier
                    .padding(10.dp, if (isFirstItem) 10.dp else 0.dp, 10.dp, 10.dp)
            ) {
                DayViewItem(exampleItems[index])
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DayViewPreview(){
    DayView()
}