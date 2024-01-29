package ru.practicum.android.diploma.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Vacancy

class SearchViewModel(
    private var searchInteractor: SearchInteractor
) : ViewModel() {
    private var stateLiveData =
        MutableLiveData<SearchScreenState>()

    fun getStateLiveData(): LiveData<SearchScreenState> {
        return stateLiveData
    }


    private var trackResultList: MutableLiveData<List<Vacancy>?> = MutableLiveData<List<Vacancy>?>()

    fun searchRequesting(searchExpression: String) {
        if (searchExpression.isNotEmpty()) {
            stateLiveData.postValue(SearchScreenState.Loading)
            viewModelScope.launch {
                stateLiveData.postValue(SearchScreenState.Loading)
                try {
                    searchInteractor.search(searchExpression).collect {
                        when (it.message) {
                            "CONNECTION_ERROR" -> stateLiveData.postValue(SearchScreenState.ConnectionError)
                            "SERVER_ERROR" -> stateLiveData.postValue(SearchScreenState.NothingFound)
                            else -> {
                                trackResultList.postValue(it.data)
                                stateLiveData.postValue(
                                    if (it.data.isNullOrEmpty())
                                        SearchScreenState.NothingFound
                                    else SearchScreenState.SearchIsOk(it.data)
                                )
                            }
                        }
                    }
                } catch (error: Error) {
                    stateLiveData.postValue(SearchScreenState.ConnectionError)
                }
            }
        }
    }

    private var vacancyHistoryList: MutableLiveData<List<Vacancy>> =
        MutableLiveData<List<Vacancy>>().apply {
            value = emptyList()
        }


    fun clearVacancyList() {
        trackResultList.value = emptyList()
    }
}
