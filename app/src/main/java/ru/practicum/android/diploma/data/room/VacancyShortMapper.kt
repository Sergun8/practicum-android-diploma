package ru.practicum.android.diploma.data.room

import ru.practicum.android.diploma.domain.models.Vacancy
//скорее всего можно убрать вместе с VacancyShort
object VacancyShortMapper {

    fun map(vacancyShort: VacancyShort): Vacancy = Vacancy(
        id = vacancyShort.id,
        area = vacancyShort.area,
        alternateUrl = vacancyShort.alternateUrl ?: "",
        employer = vacancyShort.employer ?: "",
        name = vacancyShort.name,
        salary = vacancyShort.salary
    )
}
