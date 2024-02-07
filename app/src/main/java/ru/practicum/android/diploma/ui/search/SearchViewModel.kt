package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.models.ErrorNetwork
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private var searchInteractor: SearchInteractor
) : ViewModel() {

    private val vacancys = mutableListOf<Vacancy>()
    private val pageCurrency = 0
    private val stateLiveData = MutableLiveData<SearchState>()
    private var latestSearchText: String? = null
    private val vacancySearchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY_MILLIS, viewModelScope, true) { changedText ->
        searchRequest(changedText, pageCurrency)
    }
    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }
    fun getStateLiveData(): LiveData<SearchState> {
        return stateLiveData
    }


    fun searchRequest(searchText: String, pageCurrency: Int) {
        if (searchText.isNotEmpty()) {
            renderState(SearchState.Loading)
            viewModelScope.launch {
                searchInteractor
                    .search(searchText, pageCurrency)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }
    private fun processResult(searchVacancys: List<Vacancy>?, errorMessage: ErrorNetwork?) {
        if (searchVacancys != null) {
            vacancys.addAll(searchVacancys)
        }
        when {
            errorMessage != null -> {
                renderState(SearchState.Error(errorMessage))
            }
            vacancys.isEmpty() -> {
                renderState(SearchState.Error(ErrorNetwork.NOT_FOUND))
            }
            else -> {
                renderState(SearchState.SearchContent(searchVacancys!!))
            }
        }
    }

    fun search(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            vacancySearchDebounce(changedText)
        }
    }

    companion object {

        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L

    }

}
