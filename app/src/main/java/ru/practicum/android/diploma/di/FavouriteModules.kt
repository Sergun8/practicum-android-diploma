package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.data.room.VacancyDetails
import ru.practicum.android.diploma.data.room.VacancyDetailsDto
import ru.practicum.android.diploma.data.room.VacancyRepositoryDB
import ru.practicum.android.diploma.domain.api.DeleteDataInterface
import ru.practicum.android.diploma.domain.api.GetDataByIdInterface
import ru.practicum.android.diploma.domain.api.GetDataInterface
import ru.practicum.android.diploma.domain.impl.FavouritesVacancyListRepositoryImpl
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.favourite.FavoritesViewModel

val FavouriteModule = module {
    viewModel { FavoritesViewModel(get(), get()) }
    single<DeleteDataInterface<String>>{
        VacancyRepositoryDB(get(),get())
    }
    single<GetDataInterface<List<Vacancy>>> {
        FavouritesVacancyListRepositoryImpl(get())
    }
    single<GetDataByIdInterface<Resource<VacancyDetails>>>{
        VacancyRepositoryDB(get(),get())
    }
}
