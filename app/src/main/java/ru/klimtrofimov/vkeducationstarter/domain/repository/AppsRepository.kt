package ru.klimtrofimov.vkeducationstarter.domain.repository

import ru.klimtrofimov.vkeducationstarter.domain.model.AppDetails
import ru.klimtrofimov.vkeducationstarter.domain.model.AppInfo

interface AppsRepository {
    suspend fun getApps(): List<AppInfo>
    fun getCachedApps(): List<AppInfo>
    // Карточка пока строится из уже загруженного каталога; сетевой метод не вызывается UI.
    fun getAppDetails(id: String): AppDetails
    suspend fun getRemoteAppDetails(id: String): AppDetails
}
