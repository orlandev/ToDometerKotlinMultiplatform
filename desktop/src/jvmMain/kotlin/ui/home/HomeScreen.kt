/*
 * Copyright 2021 Sergio Belda
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

package ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.sergiobelda.todometer.common.compose.ui.components.VerticalDivider
import dev.sergiobelda.todometer.common.compose.ui.task.TaskItem
import dev.sergiobelda.todometer.common.compose.ui.tasklist.TaskListItem
import dev.sergiobelda.todometer.common.compose.ui.tasklist.TaskListProgress
import dev.sergiobelda.todometer.common.compose.ui.theme.TodometerColors
import dev.sergiobelda.todometer.common.compose.ui.theme.TodometerTypography
import dev.sergiobelda.todometer.common.domain.doIfSuccess
import dev.sergiobelda.todometer.common.domain.model.Tag
import dev.sergiobelda.todometer.common.domain.model.Task
import dev.sergiobelda.todometer.common.domain.model.TaskList
import dev.sergiobelda.todometer.common.domain.usecase.GetTaskListSelectedTasksUseCase
import dev.sergiobelda.todometer.common.domain.usecase.GetTaskListSelectedUseCase
import dev.sergiobelda.todometer.common.domain.usecase.GetTaskListsUseCase
import dev.sergiobelda.todometer.common.domain.usecase.InsertTaskInTaskListSelectedUseCase
import dev.sergiobelda.todometer.common.domain.usecase.InsertTaskListUseCase
import dev.sergiobelda.todometer.common.domain.usecase.SetTaskDoingUseCase
import dev.sergiobelda.todometer.common.domain.usecase.SetTaskDoneUseCase
import dev.sergiobelda.todometer.common.domain.usecase.SetTaskListSelectedUseCase
import koin
import kotlinx.coroutines.launch
import ui.icons.iconToDometer

@Composable
fun HomeScreen() {
    var addTaskListAlertDialogState by remember { mutableStateOf(false) }
    var addTaskAlertDialogState by remember { mutableStateOf(false) }
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val setTaskDoingUseCase = koin.get<SetTaskDoingUseCase>()
    val setTaskDoneUseCase = koin.get<SetTaskDoneUseCase>()
    val getTaskListSelectedUseCase = koin.get<GetTaskListSelectedUseCase>()
    val setTaskListSelectedUseCase = koin.get<SetTaskListSelectedUseCase>()
    val getTaskListSelectedTasksUseCase = koin.get<GetTaskListSelectedTasksUseCase>()
    val getTaskListsUseCase = koin.get<GetTaskListsUseCase>()
    val insertTaskListUseCase = koin.get<InsertTaskListUseCase>()
    val insertTaskInTaskListSelectedUseCase = koin.get<InsertTaskInTaskListSelectedUseCase>()

    var taskListSelected: TaskList? by remember { mutableStateOf(null) }
    val taskListResultState by getTaskListSelectedUseCase().collectAsState(null)
    taskListResultState?.doIfSuccess { taskListSelected = it }

    var tasks: List<Task> by remember { mutableStateOf(emptyList()) }
    val tasksResultState by getTaskListSelectedTasksUseCase().collectAsState(null)
    tasksResultState?.doIfSuccess { tasks = it }

    var taskLists: List<TaskList> by remember { mutableStateOf(emptyList()) }
    val taskListsResultState by getTaskListsUseCase().collectAsState(null)
    taskListsResultState?.doIfSuccess { taskLists = it }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = iconToDometer(),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp).padding(end = 8.dp)
                        )
                        Text(text = "ToDometer")
                    }
                },
                elevation = 0.dp,
                backgroundColor = TodometerColors.surface,
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                icon = {
                    Icon(Icons.Rounded.Add, "Add task")
                },
                text = {
                    Text("Add task")
                },
                onClick = { addTaskAlertDialogState = true },
                backgroundColor = TodometerColors.primary
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        scaffoldState = scaffoldState
    ) {
        Divider()
        Column(modifier = Modifier.fillMaxSize()) {
            if (addTaskListAlertDialogState) {
                AddTaskListAlertDialog(
                    onDismissRequest = { addTaskListAlertDialogState = false }
                ) { taskListName ->
                    coroutineScope.launch {
                        insertTaskListUseCase.invoke(taskListName)
                    }
                }
            }
            if (addTaskAlertDialogState) {
                AddTaskAlertDialog(
                    onDismissRequest = { addTaskAlertDialogState = false }
                ) { title, description, _ ->
                    coroutineScope.launch {
                        insertTaskInTaskListSelectedUseCase.invoke(title, Tag.GRAY, description)
                    }
                }
            }
        }
        Row {
            TaskListsNavigationDrawer(
                taskLists,
                taskListSelected?.id ?: "",
                "My tasks",
                onTaskListClick = {
                    coroutineScope.launch {
                        setTaskListSelectedUseCase.invoke(it)
                    }
                },
                onAddTaskListClick = { addTaskListAlertDialogState = true }
            )
            VerticalDivider()
            Column {
                TaskListProgress(
                    taskListSelected?.name ?: "My tasks",
                    tasks,
                    modifier = Modifier.padding(top = 16.dp)
                )
                if (tasks.isEmpty()) {
                    EmptyTasksListView()
                } else {
                    TasksListView(
                        tasks,
                        onDoingClick = {
                            coroutineScope.launch {
                                setTaskDoingUseCase(it)
                            }
                        },
                        onDoneClick = {
                            coroutineScope.launch {
                                setTaskDoneUseCase(it)
                            }
                        },
                        onTaskItemClick = {},
                        onTaskItemLongClick = {}
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyTaskListsView(addTaskList: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center).padding(bottom = 72.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource("images/no_task_lists.svg"),
                modifier = Modifier.size(240.dp).padding(bottom = 24.dp),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Text("There's any task list", modifier = Modifier.padding(bottom = 48.dp))
            Button(onClick = addTaskList) {
                Text("Add task list")
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksListView(
    tasks: List<Task>,
    onDoingClick: (String) -> Unit,
    onDoneClick: (String) -> Unit,
    onTaskItemClick: (String) -> Unit,
    onTaskItemLongClick: (String) -> Unit
) {
    LazyColumn {
        items(tasks, key = { it.id }) {
            TaskItem(
                task = it,
                onDoingClick = onDoingClick,
                onDoneClick = onDoneClick,
                onClick = onTaskItemClick,
                onLongClick = onTaskItemLongClick,
                modifier = Modifier.animateItemPlacement()
            )
        }
    }
}

@Composable
fun EmptyTasksListView() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center).padding(bottom = 72.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource("images/no_tasks.svg"),
                modifier = Modifier.size(240.dp).padding(bottom = 24.dp),
                contentDescription = null
            )
            Text("There are any task")
        }
    }
}

@Composable
fun TaskListsNavigationDrawer(
    taskLists: List<TaskList>,
    selectedTaskListId: String,
    defaultTaskListName: String,
    onTaskListClick: (taskListId: String) -> Unit,
    onAddTaskListClick: () -> Unit
) {
    Column(modifier = Modifier.requiredWidth(280.dp).padding(top = 16.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TASK LISTS",
                style = TodometerTypography.overline
            )
            OutlinedButton(
                onClick = onAddTaskListClick,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add task list")
                Text(text = "Add task list")
            }
        }
        LazyColumn(modifier = Modifier.padding(8.dp)) {
            item {
                TaskListItem(defaultTaskListName, selectedTaskListId == "") {
                    onTaskListClick("")
                }
            }
            items(taskLists) { taskList ->
                TaskListItem(
                    taskList.name,
                    taskList.id == selectedTaskListId
                ) {
                    onTaskListClick(taskList.id)
                }
            }
        }
    }
}
