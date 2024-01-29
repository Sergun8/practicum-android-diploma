package ru.practicum.android.diploma.data.db.interactors

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.Vacancy

interface FavouriteVacancyInteractor {
    suspend fun favouritesAdd(vacancy: Vacancy)
    suspend fun favouritesDelete(vacancy: Vacancy)
    suspend fun favouritesGet(): Flow<List<Vacancy>>

}
