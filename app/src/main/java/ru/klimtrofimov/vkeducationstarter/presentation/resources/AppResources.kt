package ru.klimtrofimov.vkeducationstarter.presentation.resources

import androidx.annotation.DrawableRes
import ru.klimtrofimov.vkeducationstarter.R

// Android-ресурсы разрешаются только в presentation. Domain хранит обычные ключи.
@DrawableRes
fun appIconResource(key: String): Int = when (key) {
    "focus" -> R.drawable.ic_app_focus
    "notes" -> R.drawable.ic_app_notes
    "weather" -> R.drawable.ic_app_weather
    "music" -> R.drawable.ic_app_music
    "books" -> R.drawable.ic_app_books
    "maps" -> R.drawable.ic_app_maps
    "photo" -> R.drawable.ic_app_photo
    "tasks" -> R.drawable.ic_app_tasks
    "games" -> R.drawable.ic_app_games
    else -> R.drawable.ic_launcher_foreground
}

@DrawableRes
fun screenshotResource(key: String): Int = when (key) {
    "demo_2" -> R.drawable.demo_screenshot_2
    "demo_3" -> R.drawable.demo_screenshot_3
    else -> R.drawable.demo_screenshot_1
}
