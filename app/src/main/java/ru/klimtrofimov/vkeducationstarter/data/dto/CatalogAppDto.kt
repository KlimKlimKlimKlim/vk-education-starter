package ru.klimtrofimov.vkeducationstarter.data.dto

// Схема реального ответа GET /catalog.
data class CatalogAppDto(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val iconUrl: String,
)
