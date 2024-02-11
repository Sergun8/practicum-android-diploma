package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.domain.search.SimilarInteractor
import ru.practicum.android.diploma.domain.search.impl.SimilarInteractorImpl
import ru.practicum.android.diploma.ui.similar.SimilarViewModel

val SimilarModule = module {

    factory<SimilarInteractor> {
        SimilarInteractorImpl(searchRepository = get())
    }
    viewModel {
        SimilarViewModel(
            similarInteractor = get()
        )
    }
}
