/*
 * Copyright 2022 Sergio Belda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.sergiobelda.todometer.ui.addtasklist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import dev.sergiobelda.todometer.R
import dev.sergiobelda.todometer.common.compose.ui.components.TitledTextField
import dev.sergiobelda.todometer.common.compose.ui.theme.TodometerColors
import dev.sergiobelda.todometer.common.compose.ui.theme.onSurfaceMediumEmphasis
import dev.sergiobelda.todometer.glance.ToDometerWidgetReceiver
import org.koin.androidx.compose.getViewModel

@Composable
fun AddTaskListScreen(
    navigateUp: () -> Unit,
    addTaskListViewModel: AddTaskListViewModel = getViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    var taskListName by rememberSaveable { mutableStateOf("") }
    var taskListNameInputError by remember { mutableStateOf(false) }

    val addTaskListUiState = addTaskListViewModel.addTaskListUiState
    if (addTaskListUiState.isAdded) {
        navigateUp()
    }

    if (addTaskListUiState.errorUi != null) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = addTaskListUiState.errorUi.message ?: ""
            )
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                backgroundColor = TodometerColors.surface,
                contentColor = contentColorFor(TodometerColors.surface),
                elevation = 0.dp,
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            Icons.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = TodometerColors.onSurfaceMediumEmphasis
                        )
                    }
                },
                title = { Text(stringResource(id = R.string.add_task_list)) },
                actions = {
                    IconButton(
                        enabled = !addTaskListUiState.isAddingTaskList,
                        onClick = {
                            if (taskListName.isBlank()) {
                                taskListNameInputError = true
                            } else {
                                addTaskListViewModel.insertTaskList(taskListName)
                                ToDometerWidgetReceiver().updateData()
                            }
                        }
                    ) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = "Save",
                            tint = if (addTaskListUiState.isAddingTaskList)
                                TodometerColors.onSurfaceMediumEmphasis else TodometerColors.primary
                        )
                    }
                },
            )
        },
        content = {
            if (addTaskListUiState.isAddingTaskList) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            Column(modifier = Modifier.padding(top = 24.dp)) {
                TitledTextField(
                    title = stringResource(R.string.name),
                    value = taskListName,
                    onValueChange = {
                        taskListName = it
                        taskListNameInputError = false
                    },
                    placeholder = { Text(stringResource(id = R.string.enter_task_list_name)) },
                    singleLine = true,
                    isError = taskListNameInputError,
                    errorMessage = stringResource(R.string.field_not_empty),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                )
            }
        }
    )
}
