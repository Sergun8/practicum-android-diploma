package ru.practicum.android.diploma.presentation.search

import ru.practicum.android.diploma.domain.models.Vacancy1

sealed class SearchScreenState {
    data object DefaultSearch : SearchScreenState()
    data object Loading : SearchScreenState()
    data object NothingFound : SearchScreenState()
    data object ConnectionError : SearchScreenState()
    data class SearchIsOk(val data: List<Vacancy1>) : SearchScreenState()
}
