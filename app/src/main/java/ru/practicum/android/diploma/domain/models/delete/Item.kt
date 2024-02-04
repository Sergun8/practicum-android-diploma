package ru.practicum.android.diploma.domain.models.delete



data class Item(
    val acceptIncompleteResumes: Boolean?,
    val address: Address?,
    val alternateUrl: String?,
    val applyAlternateUrl: String?,
    val area: Area?,
    val branding: Branding?,
    val contacts: Contacts?,
    val counters: Counters?,
    val department: Department?,
    val employer: Employer?,
    val hasTest: Boolean?,
    val id: String?,
    val insiderInterview: InsiderInterview?,
    val name: String?,
    val personalDataResale: Boolean?,
    val professionalRoles: List<ProfessionalRole>?,
    val publishedAt: String?,
    val relations: List<Any>?,
    val responseLetterRequired: Boolean?,
    val responseUrl: Any?,
    val salary: Salary?,
    val schedule: Schedule?,
    val showLogoInSearch: Boolean?,
    val snippet: Snippet?,
    val sortPointDistance: Double?,
    val type: Type?,
    val url: String?
)
