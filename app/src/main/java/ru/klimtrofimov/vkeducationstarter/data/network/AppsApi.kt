package ru.klimtrofimov.vkeducationstarter.data.network

import kotlinx.coroutines.delay
import ru.klimtrofimov.vkeducationstarter.data.dto.AppDetailsDto
import ru.klimtrofimov.vkeducationstarter.data.dto.CatalogAppDto

interface AppsApi {
    suspend fun getCatalog(): List<CatalogAppDto>
    suspend fun getAppDetails(id: String): AppDetailsDto

    suspend fun getRating(id: String): Float {
        delay(1500)
        return (30..50).random() / 10f
    }
}

// Retrofit создаёт только HTTP service. Локальный suspend getRating выполняется
// обычным Kotlin-объектом, а не Retrofit proxy, требующим HTTP-аннотацию метода.
class RetrofitAppsApi(private val service: RetrofitAppsService) : AppsApi {
    override suspend fun getCatalog(): List<CatalogAppDto> = service.getCatalog()
    override suspend fun getAppDetails(id: String): AppDetailsDto = service.getAppDetails(id)
}
