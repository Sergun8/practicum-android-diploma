package ru.practicum.android.diploma.data.db.Repositories

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.Vacancy

interface FavouriteVacancyRepository {

    fun getFavourites(): Flow<List<Any>>
    suspend fun deleteFavourite(vacancy: Vacancy)
    suspend fun putFavourite(vacancy: Vacancy)
}
