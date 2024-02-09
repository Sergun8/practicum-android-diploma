
package ru.practicum.android.diploma.data.dto.field

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.domain.models.`object`.Employer

data class EmployerDto(
    @SerializedName("logo_urls")
    val logoUrls: LogoUrls?,
    val name: String?,
)
