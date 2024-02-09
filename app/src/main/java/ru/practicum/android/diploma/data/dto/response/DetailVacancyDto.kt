package ru.practicum.android.diploma.data.dto.response

import ru.practicum.android.diploma.data.dto.field.AreaDto
import ru.practicum.android.diploma.data.dto.field.Contacts
import ru.practicum.android.diploma.data.dto.field.EmployerDto
import ru.practicum.android.diploma.data.dto.field.SalaryDto
import ru.practicum.android.diploma.data.search.network.Response

class DetailVacancyDto(
    val id: String,
    val area: AreaDto?,
    val employer: EmployerDto?,
    val name: String,
    val salary: SalaryDto?,
    val contacts: Contacts?,
    val description: String?,
) : Response()
