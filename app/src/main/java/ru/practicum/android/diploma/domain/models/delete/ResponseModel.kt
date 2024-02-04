package ru.practicum.android.diploma.domain.models.delete


data class VacancyDto(
    val arguments: List<Argument>?,
    val clusters: Any?,
    val fixes: Any?,
    val found: Int?,
    val items: List<Item>?,
    val page: Int?,
    val pages: Int?,
    val perPage: Int?,
    val suggests: Any?
)
