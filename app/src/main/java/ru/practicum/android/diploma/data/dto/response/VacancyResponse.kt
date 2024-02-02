package ru.practicum.android.diploma.data.dto.response

import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.data.search.network.Response

data class VacancyResponse(val results: List<VacancyDto>) : Response()
