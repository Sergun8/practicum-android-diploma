package ru.practicum.android.diploma.domain.models

import ru.practicum.android.diploma.domain.models.`object`.Area

data class Areas(
    val area: Area,
    val id: String,
    val name: String,
    val parentId: String?
)
