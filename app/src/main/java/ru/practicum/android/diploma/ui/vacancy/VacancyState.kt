package ru.practicum.android.diploma.ui.vacancy

import ru.practicum.android.diploma.domain.models.DetailVacancy
import ru.practicum.android.diploma.domain.models.ErrorNetwork

sealed interface VacancyState {
    data object Loading : VacancyState
    data class Content(val vacancy: DetailVacancy) : VacancyState
    data class Error(val error: ErrorNetwork) : VacancyState
    data object EmptyScreen : VacancyState

}
