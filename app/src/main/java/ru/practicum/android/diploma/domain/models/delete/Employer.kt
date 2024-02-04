package ru.practicum.android.diploma.domain.models.delete


data class Employer(
    val accreditedItEmployer: Boolean?,
    val alternateUrl: String?,
    val id: String?,
    val logoUrls: LogoUrls?,
    val name: String?,
    val trusted: Boolean?,
    val url: String?
)
