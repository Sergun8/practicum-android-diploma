package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.Constant
import ru.practicum.android.diploma.data.search.network.Resource
import ru.practicum.android.diploma.domain.models.DetailVacancy
import ru.practicum.android.diploma.domain.search.VacancyInteractor

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
                    .collect { resource ->
                        processResult(resource)
                    }
            }
        }
    }

    private fun processResult(resource: Resource<DetailVacancy>) {
        if (resource != null) {
            vacancy = resource.data
        }

        when (resource.code) {
            Constant.NO_CONNECTIVITY_MESSAGE -> {
                renderState(VacancyState.Error)
            }

            Constant.SUCCESS_RESULT_CODE -> renderState(VacancyState.Content(vacancy!!))

            else -> renderState(VacancyState.Error)
        }

    }

}
