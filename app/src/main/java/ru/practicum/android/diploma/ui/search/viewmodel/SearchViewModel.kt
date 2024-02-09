package ru.practicum.android.diploma.ui.search.viewmodel

import ru.practicum.android.diploma.domain.api.SearchInteractor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.Constant.NO_CONNECTIVITY_MESSAGE
import ru.practicum.android.diploma.data.Constant.SUCCESS_RESULT_CODE
import ru.practicum.android.diploma.data.search.network.PagingInfo
import ru.practicum.android.diploma.data.search.network.Resource
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.search.state.SearchState

class SearchViewModel(
    private var searchInteractor: SearchInteractor
) : ViewModel() {

    val tempList = ArrayList<Vacancy>()
    var searchText: String = ""
    private var isNextPageLoading = false
    var currentVacanciesList: List<Vacancy> = emptyList()
    private var vacanciesNumber = ""
    private var currentPage: Int = 0
    private var maxPages: Int = 0
    private var isThereAnyProblem = false
    private val _viewStateLiveData = MutableLiveData<SearchState>()
    val viewStateLiveData: LiveData<SearchState> = _viewStateLiveData

    private var searchJob: Job? = null
    fun searchDebounce() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            search()
            if (searchText.isNotBlank()) {
                _viewStateLiveData.value = SearchState.Loading
            }
        }
    }

    fun search() {
        if (searchText.isNotBlank()) {
            if (currentPage == 0) {
                _viewStateLiveData.value = SearchState.Loading
            } else {
                _viewStateLiveData.value = SearchState.NextPageLoading(true)
                isNextPageLoading = true
            }
            viewModelScope.launch {
                val (resource, pagingInfo) = searchInteractor.search(searchText, currentPage).first()

                handleSearch(resource, pagingInfo)
            }
        }
    }

    private fun handleSearch(resource: Resource<List<Vacancy>>, pagingInfo: PagingInfo) {
        _viewStateLiveData.value = SearchState.Search

        when (resource.code) {
            NO_CONNECTIVITY_MESSAGE -> {
                handleNointernetResult()
            }

            SUCCESS_RESULT_CODE -> handleSuccessResult(resource.data, pagingInfo)

            else -> handleFailureResult()
        }

        updatePaginationInfo(pagingInfo)
    }

    // Обработка резельтата в случае отсутсвия интрнета
    private fun handleNointernetResult() {
        if (isThereAnyProblem) {
            _viewStateLiveData.value = SearchState.Loaded
            _viewStateLiveData.value = SearchState.NextPageLoading(false)
            isNextPageLoading = false
        }

        if (isNextPageLoading) {
            isThereAnyProblem = true
            _viewStateLiveData.value = SearchState.Loaded
            _viewStateLiveData.value = SearchState.NextPageLoading(false)
            isNextPageLoading = false
            _viewStateLiveData.value = SearchState.NoInternetPaging
            _viewStateLiveData.value = SearchState.Loaded
        } else {
            vacanciesNumber = ""
            _viewStateLiveData.value = SearchState.NoInternet
        }
    }

    // Обработка успешного результата
    private fun handleSuccessResult(data: List<Vacancy>?, pagingInfo: PagingInfo) {
        _viewStateLiveData.value = SearchState.Loaded
        val nextPage = pagingInfo.page ?: 0
        val foundVacancies = pagingInfo.found ?: 0
        val vacanciesData = data ?: emptyList()

        val newVacanciesList = if (isValidPage(nextPage)) {
            mergeVacanciesList(currentVacanciesList, vacanciesData)
        } else {
            vacanciesData
        }
        updateViewState(newVacanciesList, foundVacancies)

    }

    // Обработка результат в случаек пустого запроса
    private fun handleFailureResult() {
        currentVacanciesList = emptyList()
        vacanciesNumber = "0"

        _viewStateLiveData.value = SearchState.Content(
            vacancies = emptyList(),
            vacanciesNumber = "0",
            isFirstLaunch = false
        )

        isThereAnyProblem = false
    }

    private fun isValidPage(page: Int): Boolean {
        return page >= 1
    }

    private fun mergeVacanciesList(oldList: List<Vacancy>, newList: List<Vacancy>): List<Vacancy> {
        tempList.addAll(oldList)
        tempList.addAll(newList)
        return tempList
    }

    private fun updateViewState(newVacanciesList: List<Vacancy>, foundVacancies: Int) {
        _viewStateLiveData.value = SearchState.Content(
            vacancies = newVacanciesList,
            vacanciesNumber = foundVacancies.toString(),
            isFirstLaunch = false
        )

        currentVacanciesList = newVacanciesList
        vacanciesNumber = foundVacancies.toString()
        _viewStateLiveData.value = SearchState.NextPageLoading(false)
        isNextPageLoading = false
        isThereAnyProblem = false
    }

    private fun updatePaginationInfo(pagingInfo: PagingInfo) {
        currentPage = pagingInfo.page?.plus(1) ?: 0
        maxPages = pagingInfo.pages ?: 0

        if (pagingInfo.found ?: 0 <= 0) {
            _viewStateLiveData.value = SearchState.FailedToGetList
        }
    }

    fun onLastItemReached() {
        if (maxPages != currentPage && !isNextPageLoading) {
            search()
        }
    }

    companion object {

        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L

    }

}
