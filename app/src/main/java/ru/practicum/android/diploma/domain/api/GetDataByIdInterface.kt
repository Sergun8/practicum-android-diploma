package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow

interface GetDataByIdInterface<T> {
    fun getById(id: String): Flow<T?>
}
