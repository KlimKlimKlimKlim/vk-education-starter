package ru.klimtrofimov.vkeducationstarter.data.repository

import javax.inject.Inject
import ru.klimtrofimov.vkeducationstarter.data.mapper.AppMapper
import ru.klimtrofimov.vkeducationstarter.data.network.AppsApi
import ru.klimtrofimov.vkeducationstarter.data.source.AppsDataSource
import ru.klimtrofimov.vkeducationstarter.domain.model.AppDetails
import ru.klimtrofimov.vkeducationstarter.domain.model.AppInfo
import ru.klimtrofimov.vkeducationstarter.domain.repository.AppsRepository

class AppsRepositoryImpl @Inject constructor(
    private val api: AppsApi,
    private val source: AppsDataSource,
    private val mapper: AppMapper,
) : AppsRepository {
    @Volatile
    private var catalog: List<AppInfo> = emptyList()

    override suspend fun getApps(): List<AppInfo> = api.getCatalog()
        .map { mapper.toAppInfo(it) }
        .also { catalog = it }

    override fun getCachedApps(): List<AppInfo> = catalog

    override suspend fun getRemoteAppDetails(id: String): AppDetails =
        mapper.toAppDetails(api.getAppDetails(id))

    override fun getAppDetails(id: String): AppDetails {
        catalog.find { it.id == id }?.let { return mapper.toPreviewDetails(it) }
        // Только для прежних локальных карточек, не подмена ошибки сетевого каталога.
        val dto = requireNotNull(source.getApps().find { it.id == id }) {
            "Unknown application: $id"
        }
        return mapper.toAppDetails(dto)
    }
}
