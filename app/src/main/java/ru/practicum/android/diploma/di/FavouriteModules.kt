package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.favourites.DeleteDataRepositoryImpl
import ru.practicum.android.diploma.data.favourites.GetDataByIdRepositoryImpl
import ru.practicum.android.diploma.data.favourites.GetDataRepositoryImpl
import ru.practicum.android.diploma.data.favourites.SaveDataRepositoryImpl
import ru.practicum.android.diploma.data.search.network.NetworkClient
import ru.practicum.android.diploma.data.search.network.RetrofitNetworkClient
import ru.practicum.android.diploma.domain.api.DeleteDataRepository
import ru.practicum.android.diploma.domain.api.GetDataByIdRepository
import ru.practicum.android.diploma.domain.api.GetDataRepository
import ru.practicum.android.diploma.domain.api.SaveDataRepository

val FavouriteModule = module {
    single<DeleteDataRepository> {
        DeleteDataRepositoryImpl(get())
    }
    single<GetDataRepository> {
        GetDataRepositoryImpl(get())
    }
    single<GetDataByIdRepository> {
        GetDataByIdRepositoryImpl(get(), get())
    }
    single<SaveDataRepository> {
        SaveDataRepositoryImpl(get())
    }
    single<NetworkClient> {
        RetrofitNetworkClient(get(),get())
    }
}
