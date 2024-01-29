package ru.practicum.android.diploma.data.db.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.convertors.VacancyDbConvertor
import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.data.dto.Vacancy

class FavouriteVacancyRepositoryImpl(
    private val db: AppDatabase,
    private val convertor: VacancyDbConvertor,
    ) : FavouriteVacancyRepository {
        override fun getFavourites(): Flow<List<Vacancy>> = flow {
            val vacs = db.favoriteVacancyDao().getFavoriteVacancy()
            emit(convertFromTrackEntity(vacs))
        }

        override suspend fun putFavourite(vacancy: Vacancy) {
            val converted: VacancyEntity = convertor.map(vacancy)
            vacancy.isFavourite = true
            db.favoriteVacancyDao().addFavoriteVacancy(converted)
        }

        override suspend fun deleteFavourite(vacancy: Vacancy) {
            vacancy.isFavourite = false
            convertor.map(vacancy).let { db.favoriteVacancyDao().deleteFavoriteVacancy(vacancy.id) }
        }

        private fun convertFromTrackEntity(vacList: List<VacancyEntity>): List<Vacancy> {
            return vacList.map { vacancy -> convertor.map(vacancy) }
        }
}
