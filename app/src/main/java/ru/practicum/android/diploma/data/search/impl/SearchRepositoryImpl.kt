import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.Constant.NO_CONNECTIVITY_MESSAGE
import ru.practicum.android.diploma.data.Constant.PAGE
import ru.practicum.android.diploma.data.Constant.PER_PAGE
import ru.practicum.android.diploma.data.Constant.PER_PAGE_ITEMS
import ru.practicum.android.diploma.data.Constant.SERVER_ERROR
import ru.practicum.android.diploma.data.Constant.SUCCESS_RESULT_CODE
import ru.practicum.android.diploma.data.Constant.TEXT
import ru.practicum.android.diploma.data.dto.DetailVacancyDto
import ru.practicum.android.diploma.data.dto.convertors.Convertors
import ru.practicum.android.diploma.data.dto.response.SearchListDto
import ru.practicum.android.diploma.data.search.network.JobSearchRequest
import ru.practicum.android.diploma.data.search.network.NetworkClient
import ru.practicum.android.diploma.data.search.network.PagingInfo
import ru.practicum.android.diploma.data.search.network.Resource
import ru.practicum.android.diploma.data.search.network.VacancyDetailRequest
import ru.practicum.android.diploma.domain.models.DetailVacancy
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.api.SearchRepository

class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {

    override suspend fun search(expression: String, page: Int): Flow<Pair<Resource<List<Vacancy>>, PagingInfo>> = flow {
        val options = HashMap<String, String>()

        options[PAGE] = page.toString()
        options[PER_PAGE] = PER_PAGE_ITEMS
        options[TEXT] = expression

        val response = networkClient.search(JobSearchRequest(options))
        when (response.resultCode) {
            NO_CONNECTIVITY_MESSAGE -> {
                emit(Resource<List<Vacancy>>(code = NO_CONNECTIVITY_MESSAGE) to PagingInfo())
            }

            SUCCESS_RESULT_CODE -> {
                emit(
                    Resource(
                        (response as SearchListDto).results.map { vacancyDto ->
                            Convertors().convertorToVacancy(vacancyDto)
                        },
                        SUCCESS_RESULT_CODE
                    ) to PagingInfo(
                        page = response.page, pages = response.pages, found = response.found
                    )
                )
            }

            else -> {
                emit(Resource<List<Vacancy>>(code = SERVER_ERROR) to PagingInfo())
            }
        }

    }
    override fun getDetailVacancy(id: String): Flow<Resource<DetailVacancy>>  = flow {
        val response = networkClient.doRequest(VacancyDetailRequest(id))
        when (response.resultCode) {
            NO_CONNECTIVITY_MESSAGE -> {
                emit(Resource(code = NO_CONNECTIVITY_MESSAGE))
            }
            SUCCESS_RESULT_CODE -> {

                emit(Resource(Convertors().convertorToDetailVacancy(response as DetailVacancyDto)))

            }
            else -> {
                emit(Resource(code = NO_CONNECTIVITY_MESSAGE))
            }
        }
    }
}
