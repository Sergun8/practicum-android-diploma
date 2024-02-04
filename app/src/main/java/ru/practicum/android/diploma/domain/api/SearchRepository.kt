package ru.practicum.android.diploma.domain.api


import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.domain.models.SearchList
import ru.practicum.android.diploma.domain.models.Vacancy

interface SearchRepository {
    fun search(expression: String, page: Int): Flow<Resource<SearchList>>

}
