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

package dev.sergiobelda.todometer.common.data.api.client

import dev.sergiobelda.todometer.common.data.api.model.TaskApiModel
import dev.sergiobelda.todometer.common.data.api.request.NewTaskRequestBody
import dev.sergiobelda.todometer.common.data.api.request.UpdateTaskStateRequestBody

interface ITaskApiClient {

    suspend fun getTasks(): List<TaskApiModel>

    suspend fun getTasks(taskListId: String? = null): List<TaskApiModel>

    suspend fun getTask(id: String): TaskApiModel

    suspend fun insertTask(newTaskRequestBody: NewTaskRequestBody): String

    suspend fun deleteTask(id: String): String

    suspend fun updateTaskState(
        id: String,
        updateTaskRequestBody: UpdateTaskStateRequestBody
    ): String
}
