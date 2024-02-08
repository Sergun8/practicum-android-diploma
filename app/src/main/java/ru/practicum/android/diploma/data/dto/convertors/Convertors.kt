package ru.practicum.android.diploma.data.dto.convertors

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.dto.response.DetailVacancyDto
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.data.dto.field.AreaDto
import ru.practicum.android.diploma.data.dto.field.Contacts
import ru.practicum.android.diploma.data.dto.field.EmployerDto
import ru.practicum.android.diploma.data.dto.field.LogoUrls
import ru.practicum.android.diploma.data.dto.field.Phone
import ru.practicum.android.diploma.data.dto.field.SalaryDto
import ru.practicum.android.diploma.data.dto.response.SearchListDto
import ru.practicum.android.diploma.domain.models.DetailVacancy
import ru.practicum.android.diploma.domain.models.SearchList
import ru.practicum.android.diploma.domain.models.Vacancy

class Convertors {
    private fun convertorToVacancy(vacancy: VacancyDto): Vacancy {
        return Vacancy(
            id = vacancy.id,
            area = createAreaName(vacancy.area),
            alternateUrl =  createLogoUrl(vacancy.employer?.logoUrls),
            employer = createEmployerName(vacancy.employer),
            name = vacancy.name,
            salary = createSalary(vacancy.salary)
        )
    }

    fun convertorToSearchList(searchList: SearchListDto): SearchList {
        return SearchList(
            found = searchList.found,
            maxPages = searchList.pages,
            currentPages = searchList.page,
            listVacancy = searchList.results?.map { vacancyDto -> convertorToVacancy(vacancyDto) },
        )
    }

    fun convertorToDetailVacancy(vacancy: DetailVacancyDto): DetailVacancy {
        return DetailVacancy(
            id = vacancy.id,
            areaId = "",
            areaName = createAreaName(vacancy.area),
            areaUrl = createLogoUrl(vacancy.employer?.logoUrls),
            contactsCallTrackingEnabled = false,
            contactsEmail = vacancy.contacts?.email,
            contactsName = vacancy.contacts?.name,
            contactsPhones = vacancy.contacts?.phones?.let { createPhone(it) },
            description = vacancy.description,
            employerName = createEmployerName(vacancy.employer),
            employmentId = "",
            employmentName = "",
            experienceId = "",
            experienceName = "",
            keySkillsNames = listOf(),
            name = vacancy.name,
            salaryCurrency = "",
            salaryFrom = 100400,
            salaryGross = false,
            salaryTo = 222,
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

    private fun createSalary(salary: SalaryDto?): String {
        salary ?: return R.string.salary_not.toString()

        return when {
            salary.from != null && salary.to != null ->
                with(salary) {
                    "от " +
                        " $from" + " до " +
                        " $to" +
                        " $currency"
                }

            salary.from != null && salary.to == null ->
                with(salary) {
                    "от" +
                        " $from" +
                        " $currency"
                }

            salary.from == null && salary.to != null ->
                with(salary) {
                    " $to"
                    // " $currency"
                }

            else -> R.string.salary_not.toString()
        }
    }

    private fun createLogoUrl(logo: LogoUrls?): String? {
        return if (logo?.logoUrl240 == null) {
            null
        } else {
            logo.logoUrl240
        }
    }
    private fun createPhone(phone: Phone): String? {
        return if (phone==null) {
            null
        } else {
           "+${phone.country}" + " (${phone.citi})"  + " ${phone.number}"
        }
    }

}
