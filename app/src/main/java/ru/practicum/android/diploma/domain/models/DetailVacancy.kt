package ru.practicum.android.diploma.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class DetailVacancy(
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
    val scheduleName: String
)
