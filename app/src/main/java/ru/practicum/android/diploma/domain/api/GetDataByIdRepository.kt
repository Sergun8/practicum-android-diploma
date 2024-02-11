package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.data.room.VacancyDetails

interface GetDataByIdRepository {
    fun getById(id: String): Flow<Resource<VacancyDetails>>
}
