package ru.practicum.android.diploma.domain.models

data class Vacancy(
    val id: String,
    val area: String,
    val alternateUrl: String?,
    val employer: String?,
    val name: String,
    val salary: String
)
