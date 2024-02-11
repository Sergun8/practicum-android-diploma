package ru.practicum.android.diploma

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import ru.practicum.android.diploma.di.SearchModules
import ru.practicum.android.diploma.di.SimilarModule
import ru.practicum.android.diploma.di.VacancyModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(SearchModules, VacancyModule, SimilarModule)
        }
    }
}
