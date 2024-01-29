package ru.practicum.android.diploma.presentation.search

import com.bumptech.glide.load.engine.Resource
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy

interface SearchInteractor {
    fun search(expression: String): Flow<ru.practicum.android.diploma.presentation.search.Resource<List<Vacancy>>>
}
// TODO: Нужено перенести класс в нужный package и написать Impl 

