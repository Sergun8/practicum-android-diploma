package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.room.VacancyDao
import ru.practicum.android.diploma.data.room.VacancyShortMapper
import ru.practicum.android.diploma.domain.api.GetDataByIdInterface
import ru.practicum.android.diploma.domain.api.GetDataInterface
import ru.practicum.android.diploma.domain.models.Vacancy

class FavouritesVacancyListRepositoryImpl(
    private val vacancyDao: VacancyDao
) : GetDataInterface<List<Vacancy>> {

    override fun get(): Flow<List<Vacancy>?> =
        vacancyDao.getVacancyList().map { list ->
            list.map { item ->
                VacancyShortMapper.map(item)
            }
        }
}
