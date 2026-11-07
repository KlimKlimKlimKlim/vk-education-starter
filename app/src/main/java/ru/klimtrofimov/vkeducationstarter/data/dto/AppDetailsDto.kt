package ru.klimtrofimov.vkeducationstarter.data.dto

import com.google.gson.annotations.SerializedName

// Схема подробностей проверена на /catalog/{id} из практики.
// Метод API намеренно использует /{id}, как требуется в задании.
data class AppDetailsDto(
    val id: String,
    val name: String,
    val developer: String,
    val category: String,
    val ageRating: Int,
    val size: Float,
    val iconUrl: String,
    @SerializedName(value = "screenshotUrlList", alternate = ["screenshots"])
    val screenshotUrls: List<String>?,
    val description: String,
)
