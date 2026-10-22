package ru.klimtrofimov.vkeducationstarter.data

fun interface AppsRepository {
    fun getApps(): List<AppInfo>
}

object LocalAppsRepository : AppsRepository {
    override fun getApps(): List<AppInfo> = SampleApps.apps
}
