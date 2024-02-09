package ru.practicum.android.diploma.data.room

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.data.search.network.NetworkClient
import ru.practicum.android.diploma.data.search.network.RetrofitNetworkClient
import ru.practicum.android.diploma.domain.api.DeleteDataInterface
import ru.practicum.android.diploma.domain.api.GetDataInterface
import ru.practicum.android.diploma.domain.models.DetailVacancy

class VacancyRepositoryDB(
    private val dao: VacancyDao,
    private val networkClient: NetworkClient
) : DeleteDataInterface<String>, SaveDataInterface<VacancyDetails>, GetDataInterface<Resource<DetailVacancy>> {

    override suspend fun delete(data: String) {
        dao.deleteVacancy(data)
    }

    override fun getById(id: String): Flow<Resource<DetailVacancy>?> = flow {
        val vacancyFromDb = dao.getVacancyById(id)?.let { vacancyEntity ->
            VacancyConverter.map(vacancyEntity)
        }
        if (vacancyFromDb != null) {
            val response = networkClient.doRequest(VacancyDetailsSearchRequest(id))
            if (response.resultCode == RetrofitNetworkClient.CODE_SUCCESS) {
                dao.updateVacancy(
                    VacancyEntityMapper.map(
                        VacancyDetailsDtoMapper.map((response as VacancyDetailsSearchResponse).dto)
                    )
                )
                emit(
                    Resource.Success(
                        VacancyDetailsDtoMapper.map(response.dto).apply {
                            isFavoriteWrapper.isFavorite = true
                        }
                    )
                )
            } else {
                emit(Resource.Success(vacancyFromDb))
            }
        } else {
            val response = networkClient.doRequest(VacancyDetailsSearchRequest(id))
            when (response.resultCode) {
                RetrofitNetworkClient.CODE_NO_INTERNET -> emit(Resource.Error(ErrorType.NO_INTERNET))

                RetrofitNetworkClient.CODE_SUCCESS -> emit(
                    Resource.Success(VacancyDetailsDtoMapper.map((response as VacancyDetailsSearchResponse).dto))
                )

                else -> emit(Resource.Error(ErrorType.SERVER_ERROR))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun save(data: DetailVacancy?) {
        data?.run {
            dao.saveVacancy(VacancyConverter.map(this))
        }
    }

}
