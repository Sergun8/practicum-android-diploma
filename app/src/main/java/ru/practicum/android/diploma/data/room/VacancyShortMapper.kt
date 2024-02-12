package ru.practicum.android.diploma.data.room

import ru.practicum.android.diploma.domain.models.Vacancy
//скорее всего можно убрать вместе с VacancyShort
object VacancyShortMapper {

    fun map(vacancyShort: VacancyShort): Vacancy = Vacancy(
        id = vacancyShort.id,
        city = vacancyShort.area,
        employerLogoUrls = vacancyShort.alternateUrl ?: "",
        employer = vacancyShort.employer ?: "",
        name = vacancyShort.name,
        salaryTo = 0,
        salaryFrom = 0,
        currency = vacancyShort.salary

    )
}
