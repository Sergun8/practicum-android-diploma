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
    private val pageCurrency = 1
    private val stateLiveData = MutableLiveData<SearchState>()
    private val vacanciesSearchDebounce =
        debounce<String?>(SEARCH_DEBOUNCE_DELAY_MILLIS, viewModelScope, true) { query ->
            searchRequest(query!!, pageCurrency)
        }
    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    fun getStateLiveData(): LiveData<SearchState> {
        return stateLiveData
    }




    companion object {

        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L

    }

    fun searchRequest(searchText: String, pageCurrency: Int ) {
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
                renderState(SearchState.Error)
            }

            vacancys.isEmpty() -> {
                renderState(SearchState.EmptySearch)
            }

            else -> {
                renderState(SearchState.SearchContent(searchVacancys!!))
            }
        }
    }
    fun search(query: String?) {
        if (query.isNullOrBlank())
            vacancys.clear()

        vacanciesSearchDebounce(query)
    }

}

