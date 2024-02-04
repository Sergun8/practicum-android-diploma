package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.data.dto.convertors.Convertors
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.models.ErrorNetwork
import ru.practicum.android.diploma.domain.models.Vacancy

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {
    override fun search(expression: String, page: Int): Flow<Pair<List<Vacancy>?, ErrorNetwork?>> {
        return repository.search(expression, page).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data!!.listVacancy.map {vacancy -> Convertors().convertorToVacancy(vacancy) }, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

}
