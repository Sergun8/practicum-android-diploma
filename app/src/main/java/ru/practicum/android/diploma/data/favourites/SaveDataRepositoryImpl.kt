package ru.practicum.android.diploma.data.favourites

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.data.dto.response.VacancyDetailsSearchResponse
import ru.practicum.android.diploma.data.room.VacancyConverter
import ru.practicum.android.diploma.data.room.VacancyDao
import ru.practicum.android.diploma.data.room.VacancyDetails
import ru.practicum.android.diploma.data.room.VacancyDetailsConverter
import ru.practicum.android.diploma.data.room.VacancyDetailsSearchRequest
import ru.practicum.android.diploma.data.search.network.NetworkClient
import ru.practicum.android.diploma.data.search.network.RetrofitNetworkClient
import ru.practicum.android.diploma.domain.api.DeleteDataRepository
import ru.practicum.android.diploma.domain.api.GetDataByIdRepository
import ru.practicum.android.diploma.domain.api.SaveDataRepository
import ru.practicum.android.diploma.domain.models.ErrorNetwork

class SaveDataRepositoryImpl(
    private val dao: VacancyDao,
) : SaveDataRepository {

    override suspend fun save(data: VacancyDetails) {
        data?.run {
            dao.saveVacancy(VacancyConverter.map(this))
        }
    }

}
