package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.convertors.Convertors
import ru.practicum.android.diploma.data.dto.response.DetailVacancyDto
import ru.practicum.android.diploma.data.dto.response.SearchListDto
import ru.practicum.android.diploma.data.search.network.JobSearchRequest
import ru.practicum.android.diploma.data.search.network.NetworkClient
import ru.practicum.android.diploma.data.search.network.Resource
import ru.practicum.android.diploma.data.search.network.VacancyDetailVacancyRequest
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.models.DetailVacancy
import ru.practicum.android.diploma.domain.models.ErrorNetwork
import ru.practicum.android.diploma.domain.models.PagingInfo
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.Constant.PAGE
import ru.practicum.android.diploma.util.Constant.PER_PAGE
import ru.practicum.android.diploma.util.Constant.PER_PAGE_ITEMS
import ru.practicum.android.diploma.util.Constant.SERVER_ERROR
import ru.practicum.android.diploma.util.Constant.TEXT

class SearchRepositoryImpl(
    private val networkClient: NetworkClient
) : SearchRepository {

    override suspend fun search(expression: String, page: Int): Flow<Pair<Resource<List<Vacancy>>, PagingInfo>> = flow {
        val options = HashMap<String, String>()

        options[PAGE] = page.toString()
        options[PER_PAGE] = PER_PAGE_ITEMS
        options[TEXT] = expression

        val response = networkClient.search(JobSearchRequest(options))
        when (response.resultCode) {
            NO_CONNECTIVITY_MESSAGE -> {
                emit(
                    ru.practicum.android.diploma.data.search.network.Resource<List<Vacancy>>(code = NO_CONNECTIVITY_MESSAGE) to PagingInfo()
                )
            }

            SUCCESS_RESULT_CODE -> {
                emit(
                    Resource(
                        (response as SearchListDto).results?.map { vacancyDto ->
                            Vacancy(
                                id = vacancyDto.id,
                                name = vacancyDto.name,
                                area = vacancyDto.area?.name!!,
                                employer = vacancyDto.employer?.name,
                                alternateUrl = vacancyDto.employer?.name,
                                salary = ""
                            )
                        },
                        SUCCESS_RESULT_CODE
                    ) to PagingInfo(
                        page = response.page, pages = response.pages, found = response.found
                    )
                )
            }

            else -> {
                emit(Resource<List<Vacancy>>(code = SERVER_ERROR) to PagingInfo())
            }
        }

    }

    override fun getDetailVacancy(id: String): Flow<ru.practicum.android.diploma.Resource<DetailVacancy>> = flow {
        val response = networkClient.doRequest(VacancyDetailVacancyRequest(id))
        when (response.resultCode) {
            NO_CONNECTIVITY_MESSAGE -> {
                emit(ru.practicum.android.diploma.Resource.Error(ErrorNetwork.NO_CONNECTIVITY_MESSAGE))
            }

            SUCCESS_RESULT_CODE -> {
                emit(
                    ru.practicum.android.diploma.Resource.Success(
                        Convertors().convertorToDetailVacancy(response as DetailVacancyDto)
                    )
                )
            }

            BAD_REQUEST_RESULT_CODE -> {
                emit(ru.practicum.android.diploma.Resource.Error(ErrorNetwork.BAD_REQUEST_RESULT_CODE))
            }

            CAPTCHA_INPUT -> {
                emit(ru.practicum.android.diploma.Resource.Error(ErrorNetwork.CAPTCHA_INPUT))
            }

            NOT_FOUND -> {
                emit(ru.practicum.android.diploma.Resource.Error(ErrorNetwork.NOT_FOUND))
            }

            else -> {
                emit(ru.practicum.android.diploma.Resource.Error(ErrorNetwork.SERVER_ERROR_MESSAGE))
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
