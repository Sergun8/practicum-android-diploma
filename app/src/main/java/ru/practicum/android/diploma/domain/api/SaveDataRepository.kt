package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.data.room.VacancyDetails

interface SaveDataRepository {

    suspend fun save(data: VacancyDetails)
}
