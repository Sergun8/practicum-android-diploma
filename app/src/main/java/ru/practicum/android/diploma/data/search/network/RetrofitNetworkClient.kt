package ru.practicum.android.diploma.data.search.network

import retrofit2.HttpException

class RetrofitNetworkClient(
    private val service: HhApi
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        var response = Response()
        return try {
            when (dto) {
                is JobSearchRequest -> {
                    response = service.jobSearch(
                        query = dto.expression,
                        page = dto.page,
                    )
                }

            }
            response.apply { resultCode = SUCCESS_RESULT_CODE }

        } catch (exception: HttpException) {
            response.apply { resultCode = exception.code() }
        }
    }

    companion object {
        const val NO_INTERNET_CONNECTION_CODE = -1
        const val SUCCESS_RESULT_CODE = 200
        const val HH_BASE_URL = "https://api.hh.ru/"
    }
}
