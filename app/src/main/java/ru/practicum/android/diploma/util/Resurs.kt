package ru.practicum.android.diploma

import ru.practicum.android.diploma.domain.models.ErrorNetwork

sealed class Resours<T>(val data: T? = null, val message: ErrorNetwork? = null) {
    class Success<T>(data: T) : Resours<T>(data)
    class Error<T>(message: ErrorNetwork, data: T? = null) : Resours<T>(data, message)
}
