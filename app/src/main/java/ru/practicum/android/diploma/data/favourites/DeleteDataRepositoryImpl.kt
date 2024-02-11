package ru.practicum.android.diploma.data.favourites

import ru.practicum.android.diploma.data.room.VacancyDao
import ru.practicum.android.diploma.domain.api.DeleteDataRepository

class DeleteDataRepositoryImpl(private val dao: VacancyDao) : DeleteDataRepository{
    override suspend fun delete(data: String) {
        dao.deleteVacancy(data)
    }
}
