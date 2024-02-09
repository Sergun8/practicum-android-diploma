package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.search.network.PagingInfo
import ru.practicum.android.diploma.data.search.network.Resource
import ru.practicum.android.diploma.domain.models.DetailVacancy
import ru.practicum.android.diploma.domain.models.Vacancy

interface SearchRepository {
    suspend fun search(
        expression: String,
        page: Int,
    ): Flow<Pair<Resource<List<Vacancy>>, PagingInfo>>
    fun getDetailVacancy(id: String): Flow<Resource<DetailVacancy>>
}
