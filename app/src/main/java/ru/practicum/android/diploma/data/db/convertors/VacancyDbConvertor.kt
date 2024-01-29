package ru.practicum.android.diploma.data.db.convertors

import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.data.dto.Vacancy

class VacancyDbConvertor {

    fun map(vacancy: Vacancy): VacancyEntity {
        return VacancyEntity(
            vacancy.id,
            vacancy.area,
            vacancy.contacts,
            vacancy.description,
            vacancy.employment,
            vacancy.experience,
            vacancy.keySkills,
            vacancy.name,
            vacancy.salary,
            vacancy.schedule,
            isFavorite = false,
            date = 0
        )
    }

    fun map(vacancy: VacancyEntity): Vacancy {
        return Vacancy(
            vacancy.id,
            vacancy.area!!,
            vacancy.contacts,
            vacancy.description,
            vacancy.employment,
            vacancy.experience,
            vacancy.keySkills,
            vacancy.name,
            vacancy.salary,
            vacancy.schedule,
            vacancy.isFavorite,
        )
    }
}
