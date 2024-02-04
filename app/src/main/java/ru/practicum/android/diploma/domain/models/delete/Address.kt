package ru.practicum.android.diploma.domain.models.delete


data class Address(
    val building: String?,
    val city: String?,
    val description: String?,
    val lat: Double?,
    val lng: Double?,
    val metroStations: List<MetroStation>?,
    val street: String?
)
