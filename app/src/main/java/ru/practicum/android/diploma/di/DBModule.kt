package ru.practicum.android.diploma.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.room.AppDatabase
import ru.practicum.android.diploma.data.room.DetailsConverter
import ru.practicum.android.diploma.data.room.VacancyConverter
import ru.practicum.android.diploma.data.room.VacancyDetailsConverter
import ru.practicum.android.diploma.data.room.VacancyShortMapper

val DBModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
    factory { VacancyConverter }
    factory { VacancyDetailsConverter }
    factory { VacancyShortMapper }
    factory { DetailsConverter() }
}
