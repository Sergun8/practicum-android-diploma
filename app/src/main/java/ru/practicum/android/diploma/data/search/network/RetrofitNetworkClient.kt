package ru.practicum.android.diploma.data.search.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.Constant.NO_CONNECTIVITY_MESSAGE
import ru.practicum.android.diploma.data.Constant.SERVER_ERROR
import ru.practicum.android.diploma.data.Constant.SUCCESS_RESULT_CODE
import ru.practicum.android.diploma.data.search.api.HhApi
import java.io.IOException

class RetrofitNetworkClient(
    private val service: HhApi,
    private val context: Context
) : NetworkClient {

    override suspend fun search(dto: JobSearchRequest): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = NO_CONNECTIVITY_MESSAGE }
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = service.jobSearch(dto.request)
                response.apply { resultCode = SUCCESS_RESULT_CODE }
            } catch (e: IOException) {
                e.printStackTrace()
                Response().apply { resultCode = SERVER_ERROR }
            }
        }
    }
    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }

}
