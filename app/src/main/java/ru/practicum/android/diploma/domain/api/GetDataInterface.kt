package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow

interface GetDataInterface<T> {
    fun get(): Flow<T?>
}
