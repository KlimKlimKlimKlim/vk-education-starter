package ru.klimtrofimov.vkeducationstarter.presentation.appdetails

import ru.klimtrofimov.vkeducationstarter.domain.model.AppDetails

// Для статических данных нужен только Content, без сети и состояния загрузки.
object AppDetailsState {
    data class Content(
        val appDetails: AppDetails,
        val descriptionCollapsed: Boolean,
    )
}
