package ru.klimtrofimov.vkeducationstarter.data.source

import ru.klimtrofimov.vkeducationstarter.data.dto.AppDto

fun interface AppsDataSource {
    fun getApps(): List<AppDto>
}
