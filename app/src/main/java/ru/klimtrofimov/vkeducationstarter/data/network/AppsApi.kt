package ru.klimtrofimov.vkeducationstarter.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import ru.klimtrofimov.vkeducationstarter.data.dto.AppDetailsDto
import ru.klimtrofimov.vkeducationstarter.data.dto.CatalogAppDto

interface AppsApi {
    @GET("catalog")
    suspend fun getCatalog(): List<CatalogAppDto>

    // Путь строго из задания. На момент проверки сервер возвращает здесь 404.
    @GET("{id}")
    suspend fun getAppDetails(@Path("id") id: String): AppDetailsDto
}
