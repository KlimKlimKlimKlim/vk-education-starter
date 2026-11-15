package ru.klimtrofimov.vkeducationstarter

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotSame
import org.junit.Test
import ru.klimtrofimov.vkeducationstarter.data.dto.AppDto
import ru.klimtrofimov.vkeducationstarter.data.mapper.AppMapper
import ru.klimtrofimov.vkeducationstarter.domain.model.AppDetails
import ru.klimtrofimov.vkeducationstarter.domain.model.AppInfo

class AppMapperTest {
    private val dto = AppDto(
        id = "test-id",
        title = "Test name",
        shortDescription = "Short description",
        category = "Test category",
        iconKey = "test-icon",
        developer = "Test developer",
        ageRating = 12,
        sizeMb = 42.5f,
        screenshotKeys = listOf("first", "second"),
        fullDescription = "Full description",
    )
    private val mapper = AppMapper()

    @Test
    fun mapsEveryListFieldFromDto() {
        assertEquals(
            AppInfo("test-id", "Test name", "Short description", "Test category", "test-icon"),
            mapper.toAppInfo(dto),
        )
    }

    @Test
    fun mapsEveryDetailsFieldFromDto() {
        assertEquals(
            AppDetails(
                "test-id", "Test name", "Test developer", "Test category", 12, 42.5f,
                "test-icon", listOf("first", "second"), "Full description",
            ),
            mapper.toAppDetails(dto),
        )
        assertNotSame(dto.screenshotKeys, mapper.toAppDetails(dto).screenshotKeys)
    }
}
