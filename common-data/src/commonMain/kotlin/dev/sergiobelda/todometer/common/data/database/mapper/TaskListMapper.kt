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

package dev.sergiobelda.todometer.common.data.database.mapper

import dev.sergiobelda.todometer.common.data.database.TaskListEntity
import dev.sergiobelda.todometer.common.domain.model.TaskList

fun TaskListEntity.toDomain() = TaskList(
    id,
    name,
    description,
    sync
)

fun Iterable<TaskListEntity>.toDomain() = this.map {
    it.toDomain()
}

fun TaskList.toEntity() = TaskListEntity(
    id,
    name,
    description,
    sync
)
