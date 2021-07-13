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

package com.sergiobelda.todometer.common.api.client

import com.sergiobelda.todometer.common.api.model.ProjectApiModel
import com.sergiobelda.todometer.common.api.request.NewProjectRequestBody

interface IProjectApiClient {

    suspend fun getProjects(): Array<ProjectApiModel>

    suspend fun getProject(id: String): ProjectApiModel

    suspend fun insertProject(newProjectRequestBody: NewProjectRequestBody): String

    suspend fun updateProject(id: String, name: String, description: String)

    suspend fun deleteProject(id: String): String
}
