package ru.practicum.android.diploma.data.dto.response

import ru.practicum.android.diploma.data.network.Response
import ru.practicum.android.diploma.domain.models.Vacancy1

data class JobResponse(val results: List<Vacancy1>) : Response()
