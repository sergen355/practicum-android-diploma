package ru.practicum.android.diploma.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.practicum.android.diploma.domain.filters.FiltersRepository
import ru.practicum.android.diploma.data.filters.FiltersRepositoryImpl
import ru.practicum.android.diploma.data.filters.FiltersStorageRepositoryImpl
import ru.practicum.android.diploma.domain.filters.FiltersInteractor
import ru.practicum.android.diploma.domain.filters.FiltersInteractorImpl
import ru.practicum.android.diploma.presentation.filters.viewmodel.country.FiltersCountryViewModel
import ru.practicum.android.diploma.presentation.filters.viewmodel.industry.FiltersIndustryViewModel
import ru.practicum.android.diploma.presentation.filters.viewmodel.main.FiltersViewModel
import ru.practicum.android.diploma.presentation.filters.viewmodel.region.FiltersRegionViewModel

private const val FILTERS_PREFS = "FILTERS_PREFS"

val FiltersModule = module {

    singleOf(::FiltersRepositoryImpl).bind<FiltersRepository>()
    factoryOf(::FiltersInteractorImpl).bind<FiltersInteractor>()
    viewModelOf(::FiltersViewModel)
    viewModelOf(::FiltersCountryViewModel)
    viewModelOf(::FiltersRegionViewModel)
    viewModelOf(::FiltersIndustryViewModel)

    single(qualifier = named("filtersPrefs")) {
        provideFiltersPreferences(androidApplication(), FILTERS_PREFS)
    }

    single { FiltersStorageRepositoryImpl(get(named("filtersPrefs"))) }
}

private fun provideFiltersPreferences(app: Application, key: String): SharedPreferences =
    app.getSharedPreferences(key, Context.MODE_PRIVATE)
