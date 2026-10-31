package ru.klimtrofimov.vkeducationstarter

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.klimtrofimov.vkeducationstarter.data.dto.AppDto
import ru.klimtrofimov.vkeducationstarter.data.mapper.AppMapper
import ru.klimtrofimov.vkeducationstarter.data.repository.AppsRepositoryImpl
import ru.klimtrofimov.vkeducationstarter.data.source.AppsDataSource
import ru.klimtrofimov.vkeducationstarter.data.source.LocalAppsDataSource
import ru.klimtrofimov.vkeducationstarter.domain.repository.AppsRepository

class AppsRepositoryTest {
    @Test
    fun localDataContainsExactlyNineAppsAndMatchingDetails() {
        val repository: AppsRepository = AppsRepositoryImpl(LocalAppsDataSource(), AppMapper())
        val apps = repository.getApps()
        assertEquals(9, apps.size)
        assertEquals(9, apps.map { it.id }.distinct().size)
        assertEquals(listOf(3, 3, 3), apps.chunked(3).map { it.size })
        apps.forEach { app ->
            assertTrue(app.name.isNotBlank())
            assertTrue(app.description.isNotBlank())
            assertTrue(app.category.isNotBlank())
            val details = repository.getAppDetails(app.id)
            assertEquals(app.id, details.id)
            assertEquals(app.name, details.name)
            assertEquals(app.iconKey, details.iconKey)
            assertEquals(3, details.screenshotKeys.size)
        }
    }

    @Test
    fun repositoryMapsInjectedDtoSourceInsteadOfUsingUiHardcode() {
        val dto = AppDto(
            "injected", "Injected title", "Short", "Category", "icon", "Developer",
            6, 12f, listOf("screen"), "Full description",
        )
        val repository = AppsRepositoryImpl(AppsDataSource { listOf(dto) }, AppMapper())
        assertEquals("Injected title", repository.getApps().single().name)
        assertEquals("Full description", repository.getAppDetails("injected").description)
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsUnknownAppId() {
        AppsRepositoryImpl(LocalAppsDataSource(), AppMapper()).getAppDetails("missing")
    }
}
