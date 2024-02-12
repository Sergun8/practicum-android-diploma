package ru.practicum.android.diploma.data.favourites

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.room.AppDatabase
import ru.practicum.android.diploma.data.room.VacancyEntity
import ru.practicum.android.diploma.data.room.VacancyShortMapper
import ru.practicum.android.diploma.domain.api.GetDataRepository
import ru.practicum.android.diploma.domain.models.Vacancy

class GetDataRepositoryImpl(
    private val db: AppDatabase
) : GetDataRepository {

    override fun get(): Flow<List<Vacancy>> =
        db.vacancyDao().getVacancyList().map { list ->
            list.map { item ->
                VacancyShortMapper.map(item)
            }
        }
}
