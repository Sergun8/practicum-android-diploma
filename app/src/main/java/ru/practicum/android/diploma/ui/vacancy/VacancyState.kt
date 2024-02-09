package ru.practicum.android.diploma.ui.vacancy

import ru.practicum.android.diploma.domain.models.DetailVacancy

sealed interface VacancyState {
    data object Loading : VacancyState
    data class Content(val vacancy: DetailVacancy) : VacancyState
    data class Error(val error: Int) : VacancyState
    data object EmptyScreen : VacancyState

}
