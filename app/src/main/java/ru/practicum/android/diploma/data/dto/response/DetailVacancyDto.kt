package ru.practicum.android.diploma.data.dto.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.practicum.android.diploma.data.dto.field.AreaDto
import ru.practicum.android.diploma.data.dto.field.EmployerDto
import ru.practicum.android.diploma.data.dto.field.SalaryDto
import ru.practicum.android.diploma.data.search.network.Response
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.`object`.Area


class DetailVacancyDto(
    val id: String,
    val area: AreaDto?,
    val employer: EmployerDto?,
    val name: String,
    val salary: SalaryDto?,
) : Response()
