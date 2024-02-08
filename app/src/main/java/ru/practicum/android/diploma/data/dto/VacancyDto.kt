

import ru.practicum.android.diploma.domain.models.`object`.Area
import ru.practicum.android.diploma.domain.models.`object`.Employer
import ru.practicum.android.diploma.domain.models.`object`.Salary

data class VacancyDto(
    val id: String,
    val name: String,
    val area: Area,
    val employer: Employer,
    val salary: Salary?
)
