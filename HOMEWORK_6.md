# Практическое задание 6 — Clean Architecture

## Структура

```text
ru.klimtrofimov.vkeducationstarter/
├── data/
│   ├── dto/AppDto.kt
│   ├── source/AppsDataSource.kt
│   ├── source/LocalAppsDataSource.kt
│   ├── mapper/AppMapper.kt
│   └── repository/AppsRepositoryImpl.kt
├── domain/
│   ├── model/AppInfo.kt
│   ├── model/AppDetails.kt
│   └── repository/AppsRepository.kt
├── presentation/
│   ├── apps/             # Compose списка, ViewModel, состояние и Snackbar
│   ├── appdetails/       # Карточка из практики, ViewModel и витрина
│   ├── resources/        # Ключи изображений → drawable
│   ├── theme/
│   └── AppNavigation.kt
└── di/AppContainer.kt    # Связывание зависимостей и фабрики ViewModel
```

Поток данных: `LocalAppsDataSource → AppDto → AppMapper → AppInfo / AppDetails → AppsRepository → ViewModel → Compose`.

- Весь хардкод приложений находится в `data/source/LocalAppsDataSource.kt`, первоначальный формат — `AppDto`.
- `AppMapper` явно преобразует DTO в две domain-модели. Реализация репозитория вызывает Mapper, а не возвращает DTO наружу.
- Контракт `AppsRepository` находится в Domain, реализация — в Data.
- Domain не зависит от Android, Compose, Data и Presentation: ни `R`, ни `DrawableRes`, ни DTO в domain-моделях нет.
- Presentation зависит только от Domain. Даже карточка и витрина получают данные через ViewModel, а не обращаются к статическому источнику напрямую.
- Зависимости передаются в конструкторы. `di/AppContainer` связывает реализации с фабриками ViewModel; UI не создаёт репозиторий.
- Android-ресурсы разрешаются по строковым ключам в Presentation.
- Данные локальные и синхронные; сеть, БД, DI-фреймворк, лишние Gradle-модули и новые зависимости не добавлялись.
- Структура карточки из задания 4, 3 страницы витрины по 3 приложения, навигация, сохранение списка и одноразовый Snackbar из задания 5 сохранены.

## Запуск

```powershell
git fetch origin
git switch homework-6-clean-architecture
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest
```

Далее запустить `app` на телефоне через Android Studio.

## Сценарии на телефоне

1. Запустить приложение: акцентный toolbar и список из 9 приложений с иконкой, названием, описанием и категорией.
2. Повернуть экран: контент сохраняется.
3. Нажать toolbar: Snackbar «Это список приложений»; повернуть экран — сообщение не должно появиться повторно.
4. Нажать «Заметки»: карточка показывает название выбранного приложения, описание, разработчика, иконку и изображения.
5. Прокрутить до «Витрины»: ровно 3 страницы по 3 приложения. Нажатие возвращает к списку.
6. Повторить с другим приложением, проверить кнопку «Назад».
7. Отключить интернет: список, карточка и витрина продолжают работать.

## Автотесты

- `AppMapperTest` — все поля DTO преобразуются в обе domain-модели.
- `AppsRepositoryTest` — репозиторий преобразует подставной DTO-источник, 9 приложений соответствуют карточкам, неизвестный ID отклоняется.
- `AppListViewModelTest` — источник списка читается один раз, контент не меняется при нажатии toolbar.
- `AppDetailsViewModelTest` — карточка и витрина читаются один раз через domain-контракт.
- Существующие UI-тесты задания 4–5 оставлены без изменения сценариев.

Проверки сборки: `:app:assembleDebug :app:testDebugUnitTest :app:assembleDebugAndroidTest :app:lintDebug`.
UI-тесты в этой среде только компилируются; запуск на телефоне/эмуляторе не выполнялся.

## Результат проверки

- Debug APK собран успешно.
- 8 unit-тестов прошли: 7 содержательных проверок архитектуры/данных/ViewModel и 1 существующий шаблонный тест.
- Android UI-тесты успешно скомпилированы, на устройстве не запускались.
- Android Lint: ошибок нет, 22 предупреждения (в том числе о версиях библиотек и существующих ресурсах).
