package ru.practicum.android.diploma.domain.models.delete

data class Argument(
    val argument: String?,
    val clusterGroup: ClusterGroup?,
    val disableUrl: String?,
    val value: String?,
    val valueDescription: String?
)
