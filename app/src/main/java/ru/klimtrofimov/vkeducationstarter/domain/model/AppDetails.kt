package ru.klimtrofimov.vkeducationstarter.domain.model

data class AppDetails(
    val id: String,
    val name: String,
    val developer: String,
    val category: String,
    val ageRating: Int?,
    val sizeMb: Float?,
    val iconKey: String,
    val screenshotKeys: List<String>,
    val description: String,
    val iconUrl: String? = null,
    val screenshotUrls: List<String> = emptyList(),
)
