package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.impl.VacancyInteractorImpl
import ru.practicum.android.diploma.ui.vacancy.VacancyViewModel

val VacancyModule = module {

    factory<VacancyInteractor> {
        VacancyInteractorImpl(repository = get())
    }
    viewModel {
        VacancyViewModel(
            vacancyInteractor = get()
        )
    }
}
