package ru.klimtrofimov.vkeducationstarter.data.mapper

import javax.inject.Inject
import ru.klimtrofimov.vkeducationstarter.data.dto.CatalogAppDto
import ru.klimtrofimov.vkeducationstarter.data.dto.AppDetailsDto
import ru.klimtrofimov.vkeducationstarter.data.dto.AppDto
import ru.klimtrofimov.vkeducationstarter.domain.model.AppDetails
import ru.klimtrofimov.vkeducationstarter.domain.model.AppInfo

class AppMapper @Inject constructor() {
    fun toAppInfo(dto: CatalogAppDto): AppInfo = AppInfo(
        id = dto.id,
        name = dto.name,
        description = dto.description,
        category = dto.category,
        iconKey = "",
        iconUrl = dto.iconUrl,
    )

    fun toAppDetails(dto: AppDetailsDto): AppDetails = AppDetails(
        id = dto.id,
        name = dto.name,
        developer = dto.developer,
        category = dto.category,
        ageRating = dto.ageRating,
        sizeMb = dto.size,
        iconKey = "",
        screenshotKeys = emptyList(),
        description = dto.description,
        iconUrl = dto.iconUrl,
        screenshotUrls = dto.screenshotUrls.orEmpty().toList(),
    )

    // В каталоге нет размера, возраста и разработчика: не подставляем выдуманные значения.
    fun toPreviewDetails(app: AppInfo): AppDetails = AppDetails(
        id = app.id,
        name = app.name,
        developer = "",
        category = app.category,
        ageRating = null,
        sizeMb = null,
        iconKey = app.iconKey,
        screenshotKeys = emptyList(),
        description = app.description,
        iconUrl = app.iconUrl,
    )

    fun toAppInfo(dto: AppDto): AppInfo = AppInfo(
        id = dto.id,
        name = dto.title,
        description = dto.shortDescription,
        category = dto.category,
        iconKey = dto.iconKey,
    )

    fun toAppDetails(dto: AppDto): AppDetails = AppDetails(
        id = dto.id,
        name = dto.title,
        developer = dto.developer,
        category = dto.category,
        ageRating = dto.ageRating,
        sizeMb = dto.sizeMb,
        iconKey = dto.iconKey,
        screenshotKeys = dto.screenshotKeys.toList(),
        description = dto.fullDescription,
    )
}
