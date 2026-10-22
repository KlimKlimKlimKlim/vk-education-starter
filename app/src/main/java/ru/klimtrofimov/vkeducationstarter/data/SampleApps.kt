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
}
