package ru.klimtrofimov.vkeducationstarter.data.repository

import javax.inject.Inject
import ru.klimtrofimov.vkeducationstarter.data.mapper.AppMapper
import ru.klimtrofimov.vkeducationstarter.data.source.AppsDataSource
import ru.klimtrofimov.vkeducationstarter.domain.model.AppDetails
import ru.klimtrofimov.vkeducationstarter.domain.model.AppInfo
import ru.klimtrofimov.vkeducationstarter.domain.repository.AppsRepository

class AppsRepositoryImpl @Inject constructor(
    private val source: AppsDataSource,
    private val mapper: AppMapper,
) : AppsRepository {
    override fun getApps(): List<AppInfo> = source.getApps().map(mapper::toAppInfo)

    override fun getAppDetails(id: String): AppDetails {
        val dto = requireNotNull(source.getApps().find { it.id == id }) {
            "Unknown application: $id"
        }
        return mapper.toAppDetails(dto)
    }
}
