package ru.klimtrofimov.vkeducationstarter.data.mapper

import ru.klimtrofimov.vkeducationstarter.data.dto.AppDto
import ru.klimtrofimov.vkeducationstarter.domain.model.AppDetails
import ru.klimtrofimov.vkeducationstarter.domain.model.AppInfo

class AppMapper {
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
