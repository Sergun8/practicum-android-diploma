package ru.practicum.android.diploma.data.dto.response

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.data.search.network.Response

class SearchListDto(
    val found: Int?,
    val page: Int?,
    val pages: Int?,
    @SerializedName("items")
    val results: List<VacancyDto>?,
) : Response()
