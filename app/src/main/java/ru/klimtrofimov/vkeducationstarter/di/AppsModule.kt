package ru.klimtrofimov.vkeducationstarter.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ru.klimtrofimov.vkeducationstarter.data.repository.AppsRepositoryImpl
import ru.klimtrofimov.vkeducationstarter.data.source.AppsDataSource
import ru.klimtrofimov.vkeducationstarter.data.source.LocalAppsDataSource
import ru.klimtrofimov.vkeducationstarter.domain.repository.AppsRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class AppsModule {
    @Binds
    @Singleton
    abstract fun bindAppsRepository(impl: AppsRepositoryImpl): AppsRepository

    @Binds
    @Singleton
    abstract fun bindAppsDataSource(impl: LocalAppsDataSource): AppsDataSource
}
