package ru.klimtrofimov.vkeducationstarter

import java.io.IOException
import ru.klimtrofimov.vkeducationstarter.domain.model.AppDetails
import ru.klimtrofimov.vkeducationstarter.domain.model.AppInfo
import ru.klimtrofimov.vkeducationstarter.domain.repository.AppsRepository

internal class FakeAppsRepository : AppsRepository {
    val apps = listOf(AppInfo("test", "Test", "Description", "Category", "test"))
    var calls = 0
    var detailsCalls = 0
    var remoteDetailsCalls = 0
    var fail = false
    var cached: List<AppInfo> = emptyList()
    override suspend fun getApps(): List<AppInfo> {
        calls++
        if (fail) throw IOException("Offline")
        return apps.also { cached = it }
    }
    override fun getCachedApps(): List<AppInfo> = cached
    override fun getAppDetails(id: String): AppDetails {
        detailsCalls++
        check(id == "test")
        return AppDetails("test", "Test", "", "Category", null, null, "test", emptyList(), "Description")
    }
    override suspend fun getRemoteAppDetails(id: String): AppDetails {
        remoteDetailsCalls++
        error("Detailed endpoint must not be called by UI")
    }
}
