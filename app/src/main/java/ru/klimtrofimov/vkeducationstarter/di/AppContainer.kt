package ru.klimtrofimov.vkeducationstarter.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.klimtrofimov.vkeducationstarter.data.mapper.AppMapper
import ru.klimtrofimov.vkeducationstarter.data.repository.AppsRepositoryImpl
import ru.klimtrofimov.vkeducationstarter.data.source.LocalAppsDataSource
import ru.klimtrofimov.vkeducationstarter.domain.repository.AppsRepository
import ru.klimtrofimov.vkeducationstarter.presentation.appdetails.AppDetailsViewModel
import ru.klimtrofimov.vkeducationstarter.presentation.apps.AppListViewModel

// Composition root: только здесь связываются реализации data и presentation.
object AppContainer {
    private val repository: AppsRepository = AppsRepositoryImpl(LocalAppsDataSource(), AppMapper())

    val appListViewModelFactory = viewModelFactory {
        initializer { AppListViewModel(repository) }
    }

    fun appDetailsViewModelFactory(appId: String) = viewModelFactory {
        initializer { AppDetailsViewModel(appId, repository) }
    }
}
