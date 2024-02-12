package ru.practicum.android.diploma.domain.search.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.search.network.Resource
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.search.SearchRepository
import ru.practicum.android.diploma.domain.search.SimilarInteractor

class SimilarInteractorImpl(private val searchRepository: SearchRepository) : SimilarInteractor {
    override suspend fun getSimilarVacancy(id: String): Flow<Resource<List<Vacancy>>> {
        return searchRepository.getSimilarVacancy(id)
    }
}
