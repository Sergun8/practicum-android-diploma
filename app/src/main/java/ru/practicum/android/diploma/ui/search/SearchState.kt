package ru.practicum.android.diploma.ui.search

import ru.practicum.android.diploma.domain.models.Vacancy

sealed class SearchState {
    object Search : SearchState()
    object Loading : SearchState()
    object FailedToGetList : SearchState()
    data class Content(
        val vacancies: List<Vacancy>,
        val vacanciesNumber: String,
        val isFirstLaunch: Boolean
    ) : SearchState()
    data class NextPageLoading(val isLoading: Boolean) : SearchState()
}
