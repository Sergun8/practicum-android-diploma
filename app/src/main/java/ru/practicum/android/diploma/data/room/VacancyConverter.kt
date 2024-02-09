package ru.practicum.android.diploma.data.room

import ru.practicum.android.diploma.domain.models.DetailVacancy
import java.util.stream.Collectors

object VacancyConverter {
    fun map(entity: VacancyEntity): DetailVacancy = DetailVacancy(
        id = entity.id,
        areaUrl = entity.url,
        name = entity.name,
        areaName = entity.area,
        salaryCurrency = entity.salaryCurrency,
        salaryFrom = entity.salaryFrom,
        salaryTo = entity.salaryTo,
        salaryGross = entity.salaryGross,
        experienceName = entity.experience,
        scheduleId = entity.schedule,
        contactsName = entity.contactName,
        contactsEmail = entity.contactEmail,
        contactsPhones = entity.phones?.split(";"),
        scheduleName = entity.contactComment!!,
        employmentId = entity.employerUrl,
        employmentName = entity.employerName,
        keySkillsNames = entity.keySkills?.split(";")!!,
        description = entity.description,
    ).apply {
        isFavoriteWrapper.isFavorite = true
    }

    fun map(vacancy: DetailVacancy): VacancyEntity = VacancyEntity(
        id = vacancy.id,
        url = vacancy.areaUrl.toString()!!,
        name = vacancy.name,
        area = vacancy.area,
        salaryCurrency = vacancy.salaryCurrency,
        salaryFrom = vacancy.salaryFrom,
        salaryTo = vacancy.salaryTo,
        salaryGross = vacancy.salaryGross,
        experience = vacancy.experience,
        schedule = vacancy.schedule,
        contactName = vacancy.contactName,
        contactEmail = vacancy.contactEmail,
        phones = vacancy.phones?.stream()?.collect(Collectors.joining(";")),
        contactComment = vacancy.contactComment,
        logoUrl = vacancy.logoUrl,
        logoUrl90 = vacancy.logoUrl90,
        logoUrl240 = vacancy.logoUrl240,
        address = vacancy.address,
        employerUrl = vacancy.employerUrl,
        employerName = vacancy.employerName,
        employment = vacancy.employment,
        keySkills = vacancy.keySkills?.stream()?.collect(Collectors.joining(";")),
        description = vacancy.description,
    )
}
