package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.GetDataInterface
import ru.practicum.android.diploma.domain.impl.FavouritesVacancyListRepositoryImpl
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.favourite.FavoritesViewModel

val FavouriteModule = module {
    viewModel { FavoritesViewModel(get(), get()) }
    single<GetDataInterface<List<Vacancy>>> {
        FavouritesVacancyListRepositoryImpl(get())
    }
}
