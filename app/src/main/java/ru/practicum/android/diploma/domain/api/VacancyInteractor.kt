package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.DetailVacancy
import ru.practicum.android.diploma.domain.models.ErrorNetwork

interface VacancyInteractor {
    fun getDetailVacancy(id: String): Flow<Pair<DetailVacancy?, ErrorNetwork?>>
}
