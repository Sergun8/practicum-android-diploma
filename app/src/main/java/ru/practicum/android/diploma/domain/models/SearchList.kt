package ru.practicum.android.diploma.domain.models

import ru.practicum.android.diploma.data.dto.VacancyDto

data class SearchList(
    val found: Int,
    val maxPages:Int,
    val currentPages:Int,
    val listVacancy:List<VacancyDto>
)
