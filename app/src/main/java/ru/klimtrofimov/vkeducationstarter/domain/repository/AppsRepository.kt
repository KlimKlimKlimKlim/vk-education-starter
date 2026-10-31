package ru.klimtrofimov.vkeducationstarter.domain.repository

import ru.klimtrofimov.vkeducationstarter.domain.model.AppDetails
import ru.klimtrofimov.vkeducationstarter.domain.model.AppInfo

interface AppsRepository {
    fun getApps(): List<AppInfo>
    fun getAppDetails(id: String): AppDetails
}
