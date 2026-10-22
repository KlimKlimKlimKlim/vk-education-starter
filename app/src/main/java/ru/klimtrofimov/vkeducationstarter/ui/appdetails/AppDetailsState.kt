package ru.klimtrofimov.vkeducationstarter.ui.appdetails

import ru.klimtrofimov.vkeducationstarter.data.AppDetails

// Для статических данных нужен только Content, без сети и состояния загрузки.
object AppDetailsState {
    data class Content(
        val appDetails: AppDetails,
        val descriptionCollapsed: Boolean,
    )
}
