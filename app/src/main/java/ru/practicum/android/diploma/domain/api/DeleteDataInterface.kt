package ru.practicum.android.diploma.domain.api

interface DeleteDataInterface<T> {
    suspend fun delete(data: T)
}
