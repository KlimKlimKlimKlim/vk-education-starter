package ru.klimtrofimov.vkeducationstarter.data.source

import javax.inject.Inject
import ru.klimtrofimov.vkeducationstarter.data.dto.AppDto

// Вымышленные приложения: все захардкоженные данные находятся только в data.
class LocalAppsDataSource @Inject constructor() : AppsDataSource {
    override fun getApps(): List<AppDto> = apps

    private val apps = listOf(
        app("focus", "Фокус", "Таймер для работы и отдыха", "Производительность"),
        app("notes", "Заметки", "Идеи и записи всегда под рукой", "Инструменты"),
        app("weather", "Погода", "Прогноз на день и неделю", "Погода"),
        app("music", "Музыка", "Плейлисты для любого настроения", "Музыка"),
        app("books", "Книги", "Библиотека любимых историй", "Книги"),
        app("maps", "Карты", "Места и маршруты рядом с вами", "Путешествия"),
        app("photo", "Фото", "Альбомы и простое редактирование", "Фотография"),
        app("tasks", "Задачи", "Планы, списки дел и напоминания", "Производительность"),
        app("games", "Игры", "Небольшие игры для перерыва", "Игры"),
    )

    private fun app(id: String, name: String, description: String, category: String) = AppDto(
        id = id,
        title = name,
        shortDescription = description,
        category = category,
        iconKey = id,
        developer = "Учебная студия",
        ageRating = 0,
        sizeMb = 24f,
        screenshotKeys = listOf("demo_1", "demo_2", "demo_3"),
        fullDescription = "${description}. " +
            "Это демонстрационная карточка учебного приложения. " +
            "Все данные, иконки и изображения сохранены в проекте, " +
            "поэтому для просмотра не требуется подключение к интернету.",
    )
}
