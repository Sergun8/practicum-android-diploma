package ru.practicum.android.diploma.data.room

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.data.dto.response.VacancyDetailsSearchResponse
import ru.practicum.android.diploma.data.search.network.NetworkClient
import ru.practicum.android.diploma.data.search.network.RetrofitNetworkClient
import ru.practicum.android.diploma.domain.api.DeleteDataInterface
import ru.practicum.android.diploma.domain.api.GetDataByIdInterface
import ru.practicum.android.diploma.domain.api.SaveDataInterface
import ru.practicum.android.diploma.domain.models.ErrorNetwork

class VacancyRepositoryDB(
    private val dao: VacancyDao,
    private val networkClient: NetworkClient
) : DeleteDataInterface<String>, SaveDataInterface<VacancyDetails>, GetDataByIdInterface<Resource<VacancyDetails>> {

    override suspend fun delete(data: String) {
        dao.deleteVacancy(data)
    }

    override fun get(id: String): Flow<Resource<VacancyDetails>> = flow {
        val vacancyFromDb = dao.getVacancyById(id)?.let { vacancyEntity ->
            VacancyConverter.map(vacancyEntity)
        }
        if (vacancyFromDb != null) {
            val response = networkClient.doRequest(VacancyDetailsSearchRequest(id))
            if (response.resultCode == RetrofitNetworkClient.SUCCESS_RESULT_CODE) {
                dao.updateVacancy(
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

    override suspend fun save(data: VacancyDetails?) {
        data?.run {
            dao.saveVacancy(VacancyConverter.map(this))
        }
    }

}
