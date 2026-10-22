package ru.klimtrofimov.vkeducationstarter.data

import androidx.annotation.DrawableRes

data class AppInfo(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    @DrawableRes val iconRes: Int,
)
