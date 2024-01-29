package ru.practicum.android.diploma.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.practicum.android.diploma.domain.models.`object`.Area
import ru.practicum.android.diploma.domain.models.`object`.Contacts
import ru.practicum.android.diploma.domain.models.`object`.Employment
import ru.practicum.android.diploma.domain.models.`object`.Experience
import ru.practicum.android.diploma.domain.models.`object`.KeySkills
import ru.practicum.android.diploma.domain.models.`object`.Salary
import ru.practicum.android.diploma.domain.models.`object`.Schedule
@Parcelize
data class Vacancy(
    val id: String,
    val area: Area,
    val contacts: Contacts?,
    val description: String,
    val employment: Employment?,
    val experience: Experience?,
    val keySkills: ArrayList<KeySkills>,
    val name: String,
    val salary: Salary?,
    val schedule: Schedule?,
    var isFavourite: Boolean
): Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val vacancy = other as Vacancy
        return id == vacancy.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
// TODO: тут что-то нужно намудрить, потому что парс не работает с классами в переменных, а он нужен, либо нужно делать отдельный клосс вакансий под поиск с нормальными переменными
}
