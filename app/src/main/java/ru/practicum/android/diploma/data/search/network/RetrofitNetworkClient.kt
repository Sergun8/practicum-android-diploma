package ru.practicum.android.diploma.data.search.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import retrofit2.HttpException


class RetrofitNetworkClient(private val context: Context,
    private val service: HhApi,
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        var response = Response()
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        return try {
            when (dto) {
                is JobSearchRequest -> {
                    response = service.jobSearch(
                        query = dto.expression,
                        page = dto.page,
                    )

                }
                is VacancyDetailVacancyRequest -> {
                    response = service.getDetailVacancy(vacancyId = dto.id)
                }
            }
            response.apply { resultCode = SUCCESS_RESULT_CODE }

        } catch (exception: HttpException) {
            response.apply { resultCode = exception.code() }
        }
    }
    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
    companion object {
        private const val NO_INTERNET_CONNECTION_CODE = -1
        private const val SUCCESS_RESULT_CODE = 200
        const val HH_BASE_URL = "https://api.hh.ru/"
    }
}
