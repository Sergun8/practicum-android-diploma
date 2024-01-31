package ru.practicum.android.diploma

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import ru.practicum.android.diploma.presentation.search.searchModule
import ru.practicum.android.diploma.presentation.search.searchRepositoryModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                searchModule,
                searchRepositoryModule,
            )
        }
    }
}
