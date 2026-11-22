package ru.klimtrofimov.vkeducationstarter.data.dto

// Формат данных локального источника. DTO не выходит за границы data.
data class AppDto(
    val id: String,
    val title: String,
    val shortDescription: String,
    val category: String,
    val iconKey: String,
    val developer: String,
    val ageRating: Int,
    val sizeMb: Float,
    val screenshotKeys: List<String>,
    val fullDescription: String,
)
