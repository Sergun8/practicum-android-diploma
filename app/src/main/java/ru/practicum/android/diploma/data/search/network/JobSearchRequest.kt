package ru.practicum.android.diploma.data.search.network

sealed interface JobSearchRequest {
    data class VacancySearchRequest(
        val query: String,
        val page: Int,
    ) : JobSearchRequest

}

//data class JobSearchRequest(val expression: String = "Job", val page: Int)
