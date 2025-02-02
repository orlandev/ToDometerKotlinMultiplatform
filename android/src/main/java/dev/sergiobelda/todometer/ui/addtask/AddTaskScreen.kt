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

package dev.sergiobelda.todometer.ui.addtask

import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import dev.sergiobelda.todometer.R
import dev.sergiobelda.todometer.common.compose.ui.components.TitledTextField
import dev.sergiobelda.todometer.common.compose.ui.theme.TodometerColors
import dev.sergiobelda.todometer.common.compose.ui.theme.onSurfaceMediumEmphasis
import dev.sergiobelda.todometer.common.domain.model.Tag
import dev.sergiobelda.todometer.glance.ToDometerWidgetReceiver
import dev.sergiobelda.todometer.ui.components.ToDometerDateTimeSelector
import dev.sergiobelda.todometer.ui.components.ToDometerTagSelector
import org.koin.androidx.compose.getViewModel

@Composable
fun AddTaskScreen(
    navigateUp: () -> Unit,
    addTaskViewModel: AddTaskViewModel = getViewModel()
) {
    val activity = LocalContext.current as AppCompatActivity

    val scaffoldState = rememberScaffoldState()

    var taskTitle by remember { mutableStateOf("") }
    var taskTitleInputError by remember { mutableStateOf(false) }
    var taskDescription by remember { mutableStateOf("") }
    val tags = enumValues<Tag>()
    var selectedTag by remember { mutableStateOf(tags.firstOrNull() ?: Tag.GRAY) }
    var taskDueDate: Long? by remember { mutableStateOf(null) }

    val addTaskUiState = addTaskViewModel.addTaskUiState
    if (addTaskUiState.isAdded) {
        ToDometerWidgetReceiver().updateData()
        navigateUp()
    }

    if (addTaskUiState.errorUi != null) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = addTaskUiState.errorUi.message ?: ""
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
                actions = {
                    IconButton(
                        enabled = !addTaskUiState.isAddingTask,
                        onClick = {
                            if (taskTitle.isBlank()) {
                                taskTitleInputError = true
                            } else {
                                addTaskViewModel.insertTask(
                                    taskTitle,
                                    selectedTag,
                                    taskDescription,
                                    taskDueDate
                                )
                            }
                        }
                    ) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = "Save",
                            tint = if (addTaskUiState.isAddingTask)
                                TodometerColors.onSurfaceMediumEmphasis else TodometerColors.primary
                        )
                    }
                },
                title = { Text(stringResource(id = R.string.add_task)) }
            )
        },
        content = {
            if (addTaskUiState.isAddingTask) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            Column(modifier = Modifier.padding(top = 24.dp)) {
                TitledTextField(
                    title = stringResource(id = R.string.name),
                    value = taskTitle,
                    onValueChange = {
                        taskTitle = it
                        taskTitleInputError = false
                    },
                    placeholder = { Text(stringResource(id = R.string.enter_task_name)) },
                    isError = taskTitleInputError,
                    errorMessage = stringResource(id = R.string.field_not_empty),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                )
                ToDometerTagSelector(selectedTag) { tag ->
                    selectedTag = tag
                }
                ToDometerDateTimeSelector(
                    activity,
                    taskDueDate,
                    onDateTimeSelected = { taskDueDate = it },
                    onClearDateTimeClick = { taskDueDate = null }
                )
                TitledTextField(
                    title = stringResource(id = R.string.description),
                    value = taskDescription,
                    onValueChange = { taskDescription = it },
                    placeholder = { Text(stringResource(id = R.string.enter_description)) },
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
