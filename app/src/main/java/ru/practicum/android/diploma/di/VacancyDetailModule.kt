package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.data.vacancy.ExternalNavigatorImpl
import ru.practicum.android.diploma.domain.favorite.ExternalNavigator
import ru.practicum.android.diploma.domain.search.VacancyInteractor
import ru.practicum.android.diploma.domain.search.impl.VacancyInteractorImpl
import ru.practicum.android.diploma.presentation.vacancy.viewmodel.VacancyDetailViewModel

val VacancyDetailModule = module {

    factory<VacancyInteractor> {
        VacancyInteractorImpl(repository = get())
    }
    factory<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }
    viewModel {
        VacancyDetailViewModel(get(), get(), get(), get(), get(), get())
    }
}
