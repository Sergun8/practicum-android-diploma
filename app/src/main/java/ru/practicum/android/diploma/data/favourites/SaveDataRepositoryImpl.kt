package ru.practicum.android.diploma.data.favourites

import ru.practicum.android.diploma.data.room.AppDatabase
import ru.practicum.android.diploma.data.room.VacancyConverter
import ru.practicum.android.diploma.data.room.VacancyDetails
import ru.practicum.android.diploma.domain.api.SaveDataRepository

class SaveDataRepositoryImpl(
    private val db: AppDatabase,
) : SaveDataRepository {

    override suspend fun save(data: VacancyDetails) {
        data?.run {
            db.vacancyDao().saveVacancy(VacancyConverter.map(this))
        }
    }

}
