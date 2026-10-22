package ru.klimtrofimov.vkeducationstarter.data

import ru.klimtrofimov.vkeducationstarter.R

// Вымышленные приложения: демонстрационные данные для практического задания.
object SampleApps {
    val apps: List<AppInfo> = listOf(
        AppInfo("focus", "Фокус", "Таймер для работы и отдыха", "Производительность", R.drawable.ic_app_focus),
        AppInfo("notes", "Заметки", "Идеи и записи всегда под рукой", "Инструменты", R.drawable.ic_app_notes),
        AppInfo("weather", "Погода", "Прогноз на день и неделю", "Погода", R.drawable.ic_app_weather),
        AppInfo("music", "Музыка", "Плейлисты для любого настроения", "Музыка", R.drawable.ic_app_music),
        AppInfo("books", "Книги", "Библиотека любимых историй", "Книги", R.drawable.ic_app_books),
        AppInfo("maps", "Карты", "Места и маршруты рядом с вами", "Путешествия", R.drawable.ic_app_maps),
        AppInfo("photo", "Фото", "Альбомы и простое редактирование", "Фотография", R.drawable.ic_app_photo),
        AppInfo("tasks", "Задачи", "Планы, списки дел и напоминания", "Производительность", R.drawable.ic_app_tasks),
        AppInfo("games", "Игры", "Небольшие игры для перерыва", "Игры", R.drawable.ic_app_games),
    )
    val showcasePages: List<List<AppInfo>> = apps.chunked(3)

    fun details(id: String): AppDetails {
        val app = apps.first { it.id == id }
        return AppDetails(
            id = app.id,
            name = app.name,
            developer = "Учебная студия",
            category = app.category,
            ageRating = 0,
            size = 24f,
            iconRes = app.iconRes,
            screenshotResList = listOf(
                R.drawable.demo_screenshot_1,
                R.drawable.demo_screenshot_2,
                R.drawable.demo_screenshot_3,
            ),
            description = "${app.description}. " +
                "Это демонстрационная карточка учебного приложения. " +
                "Все данные, иконки и изображения сохранены в проекте, " +
                "поэтому для просмотра не требуется подключение к интернету.",
        )
    }
}
