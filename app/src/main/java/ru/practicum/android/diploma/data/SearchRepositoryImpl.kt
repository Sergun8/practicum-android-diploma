package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.data.dto.response.DetailVacancyDto
import ru.practicum.android.diploma.data.dto.convertors.Convertors
import ru.practicum.android.diploma.data.dto.response.SearchListDto
import ru.practicum.android.diploma.data.search.network.JobSearchRequest
import ru.practicum.android.diploma.data.search.network.NetworkClient
import ru.practicum.android.diploma.data.search.network.VacancyDetailVacancyRequest
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.models.DetailVacancy
import ru.practicum.android.diploma.domain.models.ErrorNetwork
import ru.practicum.android.diploma.domain.models.SearchList

class SearchRepositoryImpl(
    private val networkClient: NetworkClient
) : SearchRepository {

    override fun search(expression: String, page: Int): Flow<Resource<SearchList>> = flow {
        val response = networkClient.doRequest(JobSearchRequest(expression, page))
        when (response.resultCode) {
            NO_CONNECTIVITY_MESSAGE -> {
                emit(Resource.Error(ErrorNetwork.NO_CONNECTIVITY_MESSAGE))
            }
            SUCCESS_RESULT_CODE -> {
                emit(Resource.Success(Convertors().convertorToSearchList(response as SearchListDto)))
            }
            BAD_REQUEST_RESULT_CODE -> {
                emit(Resource.Error(ErrorNetwork.BAD_REQUEST_RESULT_CODE))
            }
            CAPTCHA_INPUT -> {
                emit(Resource.Error(ErrorNetwork.CAPTCHA_INPUT))
            }
            NOT_FOUND -> {
                emit(Resource.Error(ErrorNetwork.NOT_FOUND))
            }

            else -> {
                emit(Resource.Error(ErrorNetwork.SERVER_ERROR_MESSAGE))
            }

        }

    }

    override fun getDetailVacancy(id: String): Flow<Resource<DetailVacancy>>  = flow {
        val response = networkClient.doRequest(VacancyDetailVacancyRequest(id))
        when (response.resultCode) {
            NO_CONNECTIVITY_MESSAGE -> {
                emit(Resource.Error(ErrorNetwork.NO_CONNECTIVITY_MESSAGE))
            }

            SUCCESS_RESULT_CODE -> {
                emit(Resource.Success(Convertors().convertorToDetailVacancy(response as DetailVacancyDto)))
            }

            BAD_REQUEST_RESULT_CODE -> {
                emit(Resource.Error(ErrorNetwork.BAD_REQUEST_RESULT_CODE))
            }

            CAPTCHA_INPUT -> {
                emit(Resource.Error(ErrorNetwork.CAPTCHA_INPUT))
            }

            NOT_FOUND -> {
                emit(Resource.Error(ErrorNetwork.NOT_FOUND))
            }

            else -> {
                emit(Resource.Error(ErrorNetwork.SERVER_ERROR_MESSAGE))
            }
        }
    }

    companion object {
        private const val NO_CONNECTIVITY_MESSAGE = -1
        private const val SUCCESS_RESULT_CODE = 200
        private const val BAD_REQUEST_RESULT_CODE = 400
        private const val CAPTCHA_INPUT = 403
        private const val NOT_FOUND = 404
    }
}
