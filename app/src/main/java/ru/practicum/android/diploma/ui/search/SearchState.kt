package ru.practicum.android.diploma.ui.search

import ru.practicum.android.diploma.domain.models.ErrorNetwork
import ru.practicum.android.diploma.domain.models.Vacancy

sealed interface SearchState {
    data object Loading : SearchState
    data class SearchContent(val vacancys: List<Vacancy>) : SearchState
    data class Error(val error: ErrorNetwork) : SearchState
    data object EmptyScreen : SearchState

}
