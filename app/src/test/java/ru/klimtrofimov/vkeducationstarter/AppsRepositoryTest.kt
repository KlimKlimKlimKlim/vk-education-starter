package ru.klimtrofimov.vkeducationstarter

import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.klimtrofimov.vkeducationstarter.data.dto.AppDetailsDto
import ru.klimtrofimov.vkeducationstarter.data.dto.CatalogAppDto
import ru.klimtrofimov.vkeducationstarter.data.mapper.AppMapper
import ru.klimtrofimov.vkeducationstarter.data.network.AppsApi
import ru.klimtrofimov.vkeducationstarter.data.repository.AppsRepositoryImpl
import ru.klimtrofimov.vkeducationstarter.data.source.LocalAppsDataSource

class AppsRepositoryTest {
    private val dto = CatalogAppDto("server-id", "Server app", "From server", "Category", "https://example.org/icon.png")

    @Test
    fun catalogComesFromApiAndIsMappedToDomain() = runTest {
        val api = FakeApi()
        val repository = AppsRepositoryImpl(api, LocalAppsDataSource(), AppMapper())
        val apps = repository.getApps()
        assertEquals(listOf("server-id"), apps.map { it.id })
        assertEquals(dto.iconUrl, apps.single().iconUrl)
        assertEquals(apps, repository.getCachedApps())
        assertEquals(1, api.catalogCalls)
        assertEquals(0, api.detailsCalls)
    }

    @Test
    fun cardUsesLoadedCatalogWithoutCallingDetailedEndpoint() = runTest {
        val api = FakeApi()
        val repository = AppsRepositoryImpl(api, LocalAppsDataSource(), AppMapper())
        repository.getApps()
        val details = repository.getAppDetails(dto.id)
        assertEquals(dto.id, details.id)
        assertEquals(dto.name, details.name)
        assertEquals(dto.description, details.description)
        assertEquals(dto.iconUrl, details.iconUrl)
        assertNull(details.ageRating)
        assertNull(details.sizeMb)
        assertTrue(details.developer.isEmpty())
        assertEquals(1, api.catalogCalls)
        assertEquals(0, api.detailsCalls)
    }

    @Test
    fun remoteDetailsMethodMapsAllFieldsAndRequestedId() = runTest {
        val api = FakeApi()
        val repository = AppsRepositoryImpl(api, LocalAppsDataSource(), AppMapper())
        val details = repository.getRemoteAppDetails(dto.id)
        assertEquals(dto.id, api.requestedId)
        assertEquals("Developer", details.developer)
        assertEquals(12, details.ageRating)
        assertEquals(24f, details.sizeMb!!, 0.001f)
        assertEquals(listOf("https://example.org/screen.png"), details.screenshotUrls)
        assertEquals(0, api.catalogCalls)
        assertEquals(1, api.detailsCalls)
    }

    @Test
    fun networkFailureDoesNotFallBackToHardcodedCatalog() = runTest {
        val api = FakeApi().apply { fail = true }
        val repository = AppsRepositoryImpl(api, LocalAppsDataSource(), AppMapper())
        try {
            repository.getApps()
            throw AssertionError("Expected IOException")
        } catch (_: IOException) {
            assertTrue(repository.getCachedApps().isEmpty())
        }
    }

    private inner class FakeApi : AppsApi {
        var catalogCalls = 0
        var detailsCalls = 0
        var requestedId: String? = null
        var fail = false
        override suspend fun getCatalog(): List<CatalogAppDto> {
            catalogCalls++
            if (fail) throw IOException("Offline")
            return listOf(dto)
        }
        override suspend fun getAppDetails(id: String): AppDetailsDto {
            detailsCalls++
            requestedId = id
            return AppDetailsDto(id, dto.name, "Developer", dto.category, 12, 24f,
                dto.iconUrl, listOf("https://example.org/screen.png"), "Full description")
        }
    }
}
