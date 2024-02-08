import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.search.network.PagingInfo
import ru.practicum.android.diploma.data.search.network.Resource
import ru.practicum.android.diploma.domain.models.Vacancy

interface SearchInteractor {
    suspend fun search(
        expression: String,
        page: Int,
    ): Flow<Pair<Resource<List<Vacancy>>, PagingInfo>>
}
