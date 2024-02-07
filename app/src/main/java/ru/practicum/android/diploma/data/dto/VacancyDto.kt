package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.data.dto.field.AreaDto
import ru.practicum.android.diploma.data.dto.field.EmployerDto
import ru.practicum.android.diploma.data.dto.field.SalaryDto

data class VacancyDto(
    val id: String,
    val alternateUrl: String,
    val area: AreaDto?,
    val employer: EmployerDto?,
    val name: String,
    val salary: SalaryDto?,

)
