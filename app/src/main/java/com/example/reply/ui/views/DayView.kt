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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.reply.R
import com.example.reply.data.ItemEvent
import com.example.reply.data.ItemState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayView(
    state: ItemState,
    onEvent: (ItemEvent) -> Unit
){
    if(state.items.isNotEmpty()){
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if(state.items.isNotEmpty()){
                val itemSize = state.items.size
                items(itemSize) { index ->
                    val isFirstItem = index == 0
                    val isLastItem = index == itemSize -1

                    Box(
                        modifier = Modifier
                            .padding(10.dp, if (isFirstItem) 10.dp else 0.dp, 10.dp, if (isLastItem) 80.dp else 10.dp)
                    ) {
                        DayViewItem(state.items[index])
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = stringResource(id = R.string.no_items_title),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = stringResource(id = R.string.no_items_subtitle),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
/*
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DayViewPreview(){
    DayView()
}*/