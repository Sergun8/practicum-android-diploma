package ru.practicum.android.diploma.domain.models.`object`

import com.google.gson.annotations.SerializedName

data class Employer(
    val name: String,
    @SerializedName("logo_urls") val url: LogoUrls?
) {
    data class LogoUrls(
        @SerializedName("original") val logo: String
    )
}
