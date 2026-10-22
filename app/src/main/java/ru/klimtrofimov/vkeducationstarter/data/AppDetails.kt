package ru.klimtrofimov.vkeducationstarter.data

import androidx.annotation.DrawableRes

// Модель из практического репозитория; URL заменены локальными ресурсами.
data class AppDetails(
    val id: String,
    val name: String,
    val developer: String,
    val category: String,
    val ageRating: Int,
    val size: Float,
    @DrawableRes val iconRes: Int,
    val screenshotResList: List<Int>,
    val description: String,
)
