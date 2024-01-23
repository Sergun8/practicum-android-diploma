package ru.practicum.android.diploma.domain.models

<<<<<<< HEAD
data class Vacancy(
    val id: String,
    val area: String,
    val employer: String?,
    val name: String,
    val salary: String?
=======

import ru.practicum.android.diploma.domain.models.`object`.Area
import ru.practicum.android.diploma.domain.models.`object`.Salary
data class Vacancy (
    val id: String,
    val area: Area?,
    val name: String,
    val salary: Salary?,
>>>>>>> d8e7db1 (Настроика БД)
)
