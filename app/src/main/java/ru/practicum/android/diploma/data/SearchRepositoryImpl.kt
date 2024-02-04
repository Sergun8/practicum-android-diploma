package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.Resource.*
import ru.practicum.android.diploma.data.search.network.JobSearchRequest
import ru.practicum.android.diploma.data.search.network.NetworkClient
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.models.ErrorNetwork
import ru.practicum.android.diploma.domain.models.SearchList

class SearchRepositoryImpl(
    private val networkClient: NetworkClient
) : SearchRepository {

    override fun search(expression: String, page: Int): Flow<Resource<SearchList>> = flow {
        val response = networkClient.doRequest(JobSearchRequest.VacancySearchRequest(expression, page))
        if (response.resultCode == 200) {
            emit(Success(response as SearchList))
        } else {
            emit(Error(ErrorNetwork.SERVER_ERROR_MESSAGE))
        }
    }

    companion object {
        private const val NO_CONNECTIVITY_MESSAGE = -1
        private const val GUD = 200
    }
}




