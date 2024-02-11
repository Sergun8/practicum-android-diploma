package ru.practicum.android.diploma.data.dto.response

import ru.practicum.android.diploma.data.room.VacancyDetailsDto
import ru.practicum.android.diploma.data.search.network.Response

data class VacancyDetailsSearchResponse(
    val dto: VacancyDetailsDto
) : Response()
