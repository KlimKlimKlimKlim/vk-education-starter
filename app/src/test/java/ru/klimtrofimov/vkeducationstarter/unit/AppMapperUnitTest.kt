package ru.klimtrofimov.vkeducationstarter.unit

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.klimtrofimov.vkeducationstarter.data.dto.AppDetailsDto
import ru.klimtrofimov.vkeducationstarter.data.dto.AppDto
import ru.klimtrofimov.vkeducationstarter.data.dto.CatalogAppDto
import ru.klimtrofimov.vkeducationstarter.data.mapper.AppMapper
import ru.klimtrofimov.vkeducationstarter.domain.model.AppInfo

class AppMapperUnitTest {
    private val mapper = AppMapper()
    private val catalogDto = CatalogAppDto("1", "QuickNote", "Заметки", "Productivity", "http://i/1.png")
    private val detailsDto = AppDetailsDto(
        "1", "QuickNote", "Dev", "Productivity", 12, 25.5f, "http://i/1.png", null, "Полное описание",
    )
    private val localDto = AppDto(
        "notes", "Заметки", "Кратко", "Инструменты", "ic_notes", "VK", 6, 10f, listOf("s1", "s2"), "Полно",
    )

    @Test fun catalogDtoToDomainCopiesFields() {
        val app = mapper.toAppInfo(catalogDto)
        assertEquals(AppInfo("1", "QuickNote", "Заметки", "Productivity", "", "http://i/1.png"), app)
    }

    @Test fun detailsDtoToDomainMapsSizeAndAge() {
        val d = mapper.toAppDetails(detailsDto)
        assertEquals(25.5f, d.sizeMb!!, 0f)
        assertEquals(12, d.ageRating)
        assertEquals("Dev", d.developer)
    }

    @Test fun detailsDtoNullScreenshotsBecomeEmptyList() {
        assertTrue(mapper.toAppDetails(detailsDto).screenshotUrls.isEmpty())
    }

    @Test fun localDtoToDomainUsesTitleAndShortDescription() {
        val app = mapper.toAppInfo(localDto)
        assertEquals("Заметки", app.name)
        assertEquals("Кратко", app.description)
        assertEquals("ic_notes", app.iconKey)
    }

    @Test fun localDtoToDetailsCopiesScreenshots() {
        assertEquals(listOf("s1", "s2"), mapper.toAppDetails(localDto).screenshotKeys)
    }

    @Test fun previewDetailsHaveNoInventedValues() {
        val d = mapper.toPreviewDetails(mapper.toAppInfo(catalogDto))
        assertNull(d.ageRating)
        assertNull(d.sizeMb)
        assertEquals("", d.developer)
    }

    @Test fun domainToDtoCopiesFields() {
        val app = AppInfo("2", "Calmalist", "Список", "Lifestyle", "", "http://i/2.png")
        assertEquals(CatalogAppDto("2", "Calmalist", "Список", "Lifestyle", "http://i/2.png"), mapper.toCatalogDto(app))
    }

    @Test fun domainToDtoNullIconBecomesEmpty() {
        val app = AppInfo("3", "X", "Y", "Z", "", null)
        assertEquals("", mapper.toCatalogDto(app).iconUrl)
    }

    @Test fun dtoDomainDtoRoundTripIsLossless() {
        assertEquals(catalogDto, mapper.toCatalogDto(mapper.toAppInfo(catalogDto)))
    }
}
