package ru.practicum.android.diploma.domain.search

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.search.network.Resource
import ru.practicum.android.diploma.domain.models.Vacancy

interface SimilarInteractor {
    suspend fun getSimilarVacancy(id: String): Flow<Resource<List<Vacancy>>>
}
