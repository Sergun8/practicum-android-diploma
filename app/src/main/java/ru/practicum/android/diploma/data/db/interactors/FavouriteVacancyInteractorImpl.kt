package ru.practicum.android.diploma.data.db.interactors

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.repositories.FavouriteVacancyRepository
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
    }
}
