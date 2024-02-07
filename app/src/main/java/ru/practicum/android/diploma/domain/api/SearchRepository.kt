package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.domain.models.DetailVacancy
import ru.practicum.android.diploma.domain.models.SearchList

interface SearchRepository {
    fun search(expression: String, page: Int): Flow<Resource<SearchList>>
    fun getDetailVacancy(id: String): Flow<Resource<DetailVacancy>>
}
