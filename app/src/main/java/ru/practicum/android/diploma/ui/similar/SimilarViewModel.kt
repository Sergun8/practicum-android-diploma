package ru.practicum.android.diploma.ui.similar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.search.network.Resource
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.search.SimilarInteractor

class SimilarViewModel(
    val similarInteractor: SimilarInteractor,
) : ViewModel() {

    private val _vacancyState = MutableLiveData<SimilarState>()
    val vacancyState: LiveData<SimilarState> = _vacancyState

    private fun renderState(state: SimilarState) {
        _vacancyState.postValue(state)
    }

    fun getVacancyDetail(id: String) {
        if (id.isNotEmpty()) {
            renderState(SimilarState.Loading)
            viewModelScope.launch {
                similarInteractor
                    .getSimilarVacancy(id)
                    .collect { resource ->
                        processResult(resource)
                    }
            }
        }
    }
    private fun processResult(searchVacancys: Resource<List<Vacancy>>) {
        if (searchVacancys.data != null) {
            renderState(SimilarState.Content(searchVacancys.data))
        } else {
            renderState(SimilarState.Error)
        }
    }
}
