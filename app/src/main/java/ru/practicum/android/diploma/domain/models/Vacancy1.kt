package ru.practicum.android.diploma.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Vacancy1(
    val id: String,
    val areaId: String?,
    val areaName: String?,
    val areaUrl: String?,
    val contactsCallTrackingEnabled: Boolean?,
    val contactsEmail: String?,
    val contactsName: String?,
    val contactsPhones: List<String>?,
    val description: String,
    val employmentId: String?,
    val employmentName: String?,
    val experienceId: String?,
    val experienceName: String?,
    val keySkillsNames: List<String>,
    val name: String,
    val salaryCurrency: String?,
    val salaryFrom: Int?,
    val salaryGross: Boolean?,
    val salaryTo: Int?,
    val scheduleId: String?,
    val scheduleName: String,
    var isFavourite: Boolean
): Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val vacancy1 = other as Vacancy1
        return id == vacancy1.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
// TODO: тут что-то нужно намудрить, потому что парс не работает с классами в переменных, а он нужен, либо нужно делать отдельный клосс вакансий под поиск с нормальными переменными
}
