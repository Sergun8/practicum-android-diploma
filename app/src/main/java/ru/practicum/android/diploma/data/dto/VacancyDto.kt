import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.domain.models.Vacancy

data class VacancyDto(
    val id: String,
    val name: String,
    val area: Area,
    val employer: Employer,
    val salary: Salary?
) {
    data class Salary(
        val currency: String?,
        val from: Int,
        val to: Int
    )

    data class Area(
        val name: String
    )

    data class Employer(
        val name: String,
        val logo_urls: LogoUrls?
    ) {
        data class LogoUrls(
            val original: String
        )
    }

    fun toVacancy(): Vacancy = Vacancy(
        id = id,
        name = name,
        city = area.name,
        employer = employer.name,
        employerLogoUrls = employer.logo_urls?.original,
        currency = salary?.currency,
        salaryFrom = salary?.from,
        salaryTo = salary?.to,
    )
}
