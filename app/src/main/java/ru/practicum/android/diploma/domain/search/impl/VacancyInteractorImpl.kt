package ru.practicum.android.diploma.domain.search.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.search.network.Resource
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.DetailVacancy


class VacancyInteractorImpl(private val repository: SearchRepository) : VacancyInteractor {

    override fun getDetailVacancy(id: String): Flow<Resource<DetailVacancy>> {
        return repository.getDetailVacancy(id)
    }
}


