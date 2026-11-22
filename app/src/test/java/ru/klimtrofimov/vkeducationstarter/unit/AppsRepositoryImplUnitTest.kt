package ru.klimtrofimov.vkeducationstarter.unit

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.klimtrofimov.vkeducationstarter.data.dto.AppDetailsDto
import ru.klimtrofimov.vkeducationstarter.data.dto.AppDto
import ru.klimtrofimov.vkeducationstarter.data.dto.CatalogAppDto
import ru.klimtrofimov.vkeducationstarter.data.mapper.AppMapper
import ru.klimtrofimov.vkeducationstarter.data.network.AppsApi
import ru.klimtrofimov.vkeducationstarter.data.repository.AppsRepositoryImpl

class AppsRepositoryImplUnitTest {
    private class StubApi(
        var catalog: List<CatalogAppDto> = emptyList(),
        var error: Exception? = null,
    ) : AppsApi {
        var catalogCalls = 0
        val requestedIds = mutableListOf<String>()
        override suspend fun getCatalog(): List<CatalogAppDto> {
            catalogCalls++
            error?.let { throw it }
            return catalog
        }
        override suspend fun getAppDetails(id: String): AppDetailsDto {
            requestedIds += id
            return AppDetailsDto(id, "N$id", "Dev", "Cat", 3, 1f, "u", listOf("a"), "D")
        }
        override suspend fun getRating(id: String): Float = 4.2f
    }

    private val local = AppDto("local", "Local", "s", "c", "ic", "dev", 0, 2f, emptyList(), "full")
    private fun dto(id: String) = CatalogAppDto(id, "App$id", "d", "c", "u$id")
    private fun repo(api: StubApi) = AppsRepositoryImpl(api, { listOf(local) }, AppMapper())

    @Test fun getAppsMapsCatalog() = runTest {
        val apps = repo(StubApi(listOf(dto("1"), dto("2")))).getApps()
        assertEquals(listOf("App1", "App2"), apps.map { it.name })
    }

    @Test fun getAppsCachesResult() = runTest {
        val r = repo(StubApi(listOf(dto("1"))))
        assertTrue(r.getCachedApps().isEmpty())
        r.getApps()
        assertEquals("1", r.getCachedApps().single().id)
    }

    @Test(expected = IllegalStateException::class)
    fun getAppsPropagatesError() = runTest {
        repo(StubApi(error = IllegalStateException("network"))).getApps()
    }

    @Test fun getAppDetailsUsesCatalogFirst() = runTest {
        val r = repo(StubApi(listOf(dto("1"))))
        r.getApps()
        assertEquals("App1", r.getAppDetails("1").name)
    }

    @Test fun getAppDetailsFallsBackToLocalSource() {
        assertEquals("Local", repo(StubApi()).getAppDetails("local").name)
    }

    @Test(expected = IllegalArgumentException::class)
    fun getAppDetailsUnknownIdThrows() {
        repo(StubApi()).getAppDetails("missing")
    }

    @Test fun getRemoteAppDetailsRequestsIdAndMaps() = runTest {
        val api = StubApi()
        val d = repo(api).getRemoteAppDetails("7")
        assertEquals(listOf("7"), api.requestedIds)
        assertEquals("N7", d.name)
        assertEquals(listOf("a"), d.screenshotUrls)
    }

    @Test fun getRatingDelegatesToApi() = runTest {
        assertEquals(4.2f, repo(StubApi()).getRating("1"), 0f)
    }
}
