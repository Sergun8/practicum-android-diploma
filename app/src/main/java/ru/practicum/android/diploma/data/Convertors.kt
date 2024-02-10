package ru.practicum.android.diploma.data

import VacancyDto
import ru.practicum.android.diploma.data.dto.DetailVacancyDto
import ru.practicum.android.diploma.data.dto.field.AreaDto
import ru.practicum.android.diploma.data.dto.field.EmployerDto
import ru.practicum.android.diploma.data.dto.field.PhonesDto
import ru.practicum.android.diploma.data.dto.response.SearchListDto
import ru.practicum.android.diploma.domain.models.DetailVacancy
import ru.practicum.android.diploma.domain.models.SearchList
import ru.practicum.android.diploma.domain.models.Vacancy

class Convertors {
    fun convertorToVacancy(vacancy: VacancyDto): Vacancy {
        return Vacancy(
            id = vacancy.id,
            city = createAreaName(vacancy.area),
            employerLogoUrls = vacancy.employer?.logoUrls?.logoUrl240,
            employer = createEmployerName(vacancy.employer),
            name = vacancy.name,
            currency = vacancy.salary?.currency,
            salaryFrom = vacancy.salary?.from,
            salaryTo = vacancy.salary?.to,
        )
    }

    fun convertorToSearchList(searchList: SearchListDto): SearchList {
        return SearchList(
            found = searchList.found,
            maxPages = searchList.pages,
            currentPages = searchList.page,
            listVacancy = searchList.results.map { vacancyDto -> convertorToVacancy(vacancyDto) },
        )
    }

    fun convertorToDetailVacancy(vacancy: DetailVacancyDto): DetailVacancy {
        return DetailVacancy(
            id = vacancy.id,
            areaId = "",
            areaName = createAreaName(vacancy.area),
            areaUrl = vacancy.employer?.logoUrls?.logoUrl240,
            contactsCallTrackingEnabled = false,
            contactsEmail = vacancy.contacts?.email,
            contactsName = vacancy.contacts?.name,
            contactsPhones = vacancy.contacts?.phones.let { list -> list?.map { createPhone(it) } },
            description = vacancy.description,
            employerName = createEmployerName(vacancy.employer),
            employmentId = "",
            employmentName = "",
            experienceId = "",
            experienceName = "",
            keySkillsNames = listOf(),
            name = vacancy.name,
            salaryCurrency = vacancy.salary?.currency,
            salaryFrom = vacancy.salary?.from,
            salaryGross = false,
            salaryTo = vacancy.salary?.to,
            scheduleId = "",
            scheduleName = "",
        )
    }

    private fun createAreaName(area: AreaDto?): String {
        return if (area?.name == null) {
            "null"
        } else {
            area.name
        }
    }

    private fun createEmployerName(employerName: EmployerDto?): String {
        return if (employerName?.name == null) {
            "null"
        } else {
            employerName.name
        }
    }

    private fun createPhone(phone: PhonesDto): String {
        return "+${phone.country}" + " (${phone.city})" + " ${phone.number}"
    }

}
