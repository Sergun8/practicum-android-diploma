package ru.practicum.android.diploma.domain.api

interface SaveDataInterface<T> {

    suspend fun save(data: T?)
}
