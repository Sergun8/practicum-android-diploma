package ru.practicum.android.diploma.data.dto.response

import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.data.search.network.Response

data class JobResponse(
    val found: Int?,
    val page: Int?,
    val pages: Int?,
    val results: List<VacancyDto>?,
) : Response()
