package com.sergiobelda.todometer.common.repository

import com.sergiobelda.todometer.common.datasource.Result
import com.sergiobelda.todometer.common.model.Tag
import kotlinx.coroutines.flow.Flow

interface ITagRepository {

    fun getTags(): Flow<Result<List<Tag>>>
}