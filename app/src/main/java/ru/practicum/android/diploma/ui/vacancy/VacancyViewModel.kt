package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacancyInteractor

import ru.practicum.android.diploma.domain.models.DetailVacancy
import ru.practicum.android.diploma.domain.models.ErrorNetwork


class VacancyViewModel(
    val vacancyInteractor: VacancyInteractor,
) : ViewModel() {

    private val _vacancyState = MutableLiveData<VacancyState>()
    val vacancyState: LiveData<VacancyState> = _vacancyState
    private var vacancy: DetailVacancy? = null

    private fun renderState(state: VacancyState) {
        _vacancyState.postValue(state)
    }
    fun getVacancyDetail(id: String) {

        if (id.isNotEmpty()) {
            renderState(VacancyState.Loading)
            viewModelScope.launch {
                vacancyInteractor
                    .getDetailVacancy(id)
                    .collect { pair ->
                        processResult(pair.data, pair.code)
                    }
            }
        }
    }
    private fun processResult(detailVacancy: DetailVacancy?, errorMessage: Int?) {
        if (detailVacancy != null) {
            vacancy = detailVacancy
        }
        when {
            errorMessage != null -> {
                renderState(VacancyState.Error(errorMessage))
            }

            else -> {
                renderState(VacancyState.Content(vacancy!!))
            }
        }
    }

}
