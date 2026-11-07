package ru.klimtrofimov.vkeducationstarter

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import ru.klimtrofimov.vkeducationstarter.domain.model.AppDetails
import ru.klimtrofimov.vkeducationstarter.domain.model.AppInfo
import ru.klimtrofimov.vkeducationstarter.domain.repository.AppsRepository
import ru.klimtrofimov.vkeducationstarter.presentation.appdetails.AppDetailsViewModel

class AppDetailsViewModelTest {
    @Test
    fun cardAndShowcaseAreReadOnceFromDomainRepository() {
        var listCalls = 0
        var detailsCalls = 0
        val app = AppInfo("test", "Test", "Short", "Category", "icon")
        val details = AppDetails("test", "Test", "Dev", "Category", 0, 1f, "icon", emptyList(), "Full")
        val repository = object : AppsRepository {
            override fun getApps(): List<AppInfo> {
                listCalls += 1
                return listOf(app)
            }
            override fun getAppDetails(id: String): AppDetails {
                assertEquals("test", id)
                detailsCalls += 1
                return details
            }
        }
        val viewModel = AppDetailsViewModel(SavedStateHandle(mapOf("appId" to "test")), repository)
        assertSame(details, viewModel.uiState.value.app)
        assertEquals(listOf(app), viewModel.uiState.value.showcaseApps)
        assertEquals(1, listCalls)
        assertEquals(1, detailsCalls)
    }
}
