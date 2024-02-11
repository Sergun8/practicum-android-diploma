package ru.practicum.android.diploma.data.favourites

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.data.dto.response.VacancyDetailsSearchResponse
import ru.practicum.android.diploma.data.room.AppDatabase
import ru.practicum.android.diploma.data.room.VacancyConverter
import ru.practicum.android.diploma.data.room.VacancyDetails
import ru.practicum.android.diploma.data.room.VacancyDetailsConverter
import ru.practicum.android.diploma.data.room.VacancyDetailsSearchRequest
import ru.practicum.android.diploma.data.search.network.NetworkClient
import ru.practicum.android.diploma.data.search.network.RetrofitNetworkClient
import ru.practicum.android.diploma.domain.api.GetDataByIdRepository
import ru.practicum.android.diploma.domain.models.ErrorNetwork

class GetDataByIdRepositoryImpl(
    private val db: AppDatabase,
    private val networkClient: NetworkClient
) : GetDataByIdRepository {
    override fun getById(id: String): Flow<Resource<VacancyDetails>> = flow {
        val vacancyFromDb = db.vacancyDao().getVacancyById(id)?.let { vacancyEntity ->
            VacancyConverter.map(vacancyEntity)
        }
        if (vacancyFromDb != null) {
            val response = networkClient.doRequest(VacancyDetailsSearchRequest(id))
            if (response.resultCode == RetrofitNetworkClient.SUCCESS_RESULT_CODE) {
                db.vacancyDao().updateVacancy(
                    VacancyConverter.map(
                        VacancyDetailsConverter.map((response as VacancyDetailsSearchResponse).dto)
                    )
                )
                emit(
                    Resource.Success(
                        VacancyDetailsConverter.map(response.dto).apply {
                            isFavorite.isFavorite = true
                        }
                    )
                )
            } else {
                emit(Resource.Success(vacancyFromDb))
            }
        } else {
            val response = networkClient.doRequest(VacancyDetailsSearchRequest(id))
            when (response.resultCode) {
                RetrofitNetworkClient.NO_INTERNET_CONNECTION_CODE -> emit(Resource.Error(ErrorNetwork.NO_CONNECTIVITY_MESSAGE))

                RetrofitNetworkClient.SUCCESS_RESULT_CODE -> emit(
                    Resource.Success(VacancyDetailsConverter.map((response as VacancyDetailsSearchResponse).dto))
                )

                else -> emit(Resource.Error(ErrorNetwork.SERVER_ERROR_MESSAGE))
            }
        }
    }.flowOn(Dispatchers.IO)
}
