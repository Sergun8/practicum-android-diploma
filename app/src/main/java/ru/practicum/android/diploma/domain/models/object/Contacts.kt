package ru.practicum.android.diploma.domain.models.`object`

data class Contacts (
    val callTrackingEnabled: Boolean?,
    val email: String?,
    val name: String?,
    val phones: List<String>?
)
