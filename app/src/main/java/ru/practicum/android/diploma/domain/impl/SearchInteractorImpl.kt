package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.search.network.Resource
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.models.PagingInfo
import ru.practicum.android.diploma.domain.models.Vacancy

class SearchInteractorImpl(private val searchRepository: SearchRepository) : SearchInteractor {
    override suspend fun search(expression: String, page: Int): Flow<Pair<Resource<List<Vacancy>>, PagingInfo>> {
        return searchRepository.search(expression, page)
    }
}
