package ru.practicum.android.diploma.data.search.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RetrofitNetworkClient(
    private val context: Context,
    private val service: HhApi,
) : NetworkClient {
    companion object {
        private const val NO_INTERNET_CONNECTION_CODE = -1
        private const val SUCCESS_RESULT_CODE = 200
        private const val BAD_REQUEST_RESULT_CODE = 400
        private const val SERVER_ERROR_RESULT_CODE = 500
        const val HH_BASE_URL = "https://api.hh.ru/"
    }


    override suspend fun doRequest(request: JobSearchRequest): Response {

        if (!isConnected()) {
            return Response().apply { resultCode = NO_INTERNET_CONNECTION_CODE }
        }

        var response = Response()
        return try {
            when (request) {
                is JobSearchRequest.VacancySearchRequest -> {
                    response = service.jobSearch(
                        query = request.query,
                        page = request.page,
                    )
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

    private fun setSuccessResultCode(vararg responses: Response) {
        responses.forEach { it.resultCode = SUCCESS_RESULT_CODE }
    }
}
