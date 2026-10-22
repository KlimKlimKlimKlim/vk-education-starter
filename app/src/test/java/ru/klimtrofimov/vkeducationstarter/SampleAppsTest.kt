package ru.klimtrofimov.vkeducationstarter

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.klimtrofimov.vkeducationstarter.data.SampleApps

class SampleAppsTest {
    @Test
    fun showcaseContainsExactlyThreePagesOfThreeDistinctApps() {
        assertEquals(3, SampleApps.showcasePages.size)
        assertTrue(SampleApps.showcasePages.all { it.size == 3 })
        assertEquals(9, SampleApps.showcasePages.flatten().map { it.id }.distinct().size)
    }

    @Test
    fun allAppsHaveListFieldsAndMatchingDetails() {
        SampleApps.apps.forEach { app ->
            assertTrue(app.name.isNotBlank())
            assertTrue(app.description.isNotBlank())
            assertTrue(app.category.isNotBlank())
            val details = SampleApps.details(app.id)
            assertEquals(app.id, details.id)
            assertEquals(app.name, details.name)
            assertEquals(app.iconRes, details.iconRes)
            assertEquals(3, details.screenshotResList.size)
        }
    }
}
