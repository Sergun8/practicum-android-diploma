package ru.practicum.android.diploma.ui.similar

import ru.practicum.android.diploma.domain.models.Vacancy

sealed interface SimilarState {
    data object Loading : SimilarState
    data class Content(val vacancies: List<Vacancy>) : SimilarState
    data class Error(val r: String) : SimilarState

}
