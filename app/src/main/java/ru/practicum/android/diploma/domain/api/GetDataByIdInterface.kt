package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow

interface GetDataByIdInterface<T> {
    fun get(id: String): Flow<T?>
}
