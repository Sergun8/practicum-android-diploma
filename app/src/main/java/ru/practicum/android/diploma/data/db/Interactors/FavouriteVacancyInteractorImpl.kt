package ru.practicum.android.diploma.data.db.Interactors

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.Repositories.FavouriteVacancyRepository
import ru.practicum.android.diploma.data.dto.Vacancy

class FavouriteVacancyInteractorImpl(private val repository: FavouriteVacancyRepository) :
    FavouriteVacancyInteractor {
    override suspend fun favouritesAdd(track: Vacancy) {
        repository.putFavourite(track)
    }

    override suspend fun favouritesDelete(track: Vacancy) {
        repository.deleteFavourite(track)
    }

    override suspend fun favouritesGet(): Flow<List<Vacancy>> {
        return repository.getFavourites() as Flow<List<Vacancy>>
        TODO( "хз будет ли так работать, надо тестировать")
    }

    /*override suspend fun favouritesCheck(id: Long): Flow<Boolean> {
        return repository.checkOnLike(id)
     }
    */
}
