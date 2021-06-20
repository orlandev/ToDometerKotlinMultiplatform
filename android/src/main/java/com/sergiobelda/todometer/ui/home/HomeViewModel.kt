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

package com.sergiobelda.todometer.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sergiobelda.todometer.common.datasource.Result
import com.sergiobelda.todometer.common.model.Project
import com.sergiobelda.todometer.common.model.ProjectTasks
import com.sergiobelda.todometer.common.model.Tag
import com.sergiobelda.todometer.common.model.TaskTag
import com.sergiobelda.todometer.common.usecase.DeleteTaskUseCase
import com.sergiobelda.todometer.common.usecase.GetProjectSelectedUseCase
import com.sergiobelda.todometer.common.usecase.GetProjectsUseCase
import com.sergiobelda.todometer.common.usecase.GetTagsUseCase
import com.sergiobelda.todometer.common.usecase.GetTasksUseCase
import com.sergiobelda.todometer.common.usecase.RefreshProjectSelectedUseCase
import com.sergiobelda.todometer.common.usecase.RefreshProjectsUseCase
import com.sergiobelda.todometer.common.usecase.SetProjectSelectedUseCase
import com.sergiobelda.todometer.common.usecase.SetTaskDoingUseCase
import com.sergiobelda.todometer.common.usecase.SetTaskDoneUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val setTaskDoingUseCase: SetTaskDoingUseCase,
    private val setTaskDoneUseCase: SetTaskDoneUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val setProjectSelectedUseCase: SetProjectSelectedUseCase,
    private val refreshProjectsUseCase: RefreshProjectsUseCase,
    private val refreshProjectSelectedUseCase: RefreshProjectSelectedUseCase,
    getProjectSelectedUseCase: GetProjectSelectedUseCase,
    getProjectsUseCase: GetProjectsUseCase,
    getTasksUseCase: GetTasksUseCase,
    getTagsUseCase: GetTagsUseCase
) : ViewModel() {

    val tasks: LiveData<Result<List<TaskTag>>> = getTasksUseCase().asLiveData()

    val projects: LiveData<Result<List<Project>>> = getProjectsUseCase().asLiveData()

    val projectSelected: LiveData<Result<ProjectTasks?>> = getProjectSelectedUseCase().asLiveData()

    val tags: LiveData<Result<List<Tag>>> = getTagsUseCase().asLiveData()

    init {
        viewModelScope.launch {
            refreshProjectsUseCase()
            refreshProjectSelectedUseCase()
        }
    }

    fun deleteTask(id: String) = viewModelScope.launch {
        deleteTaskUseCase(id)
    }

    fun setTaskDoing(id: String) = viewModelScope.launch {
        setTaskDoingUseCase(id)
    }

    fun setTaskDone(id: String) = viewModelScope.launch {
        setTaskDoneUseCase(id)
    }

    fun setProjectSelected(id: String) = viewModelScope.launch {
        setProjectSelectedUseCase(id)
    }
}