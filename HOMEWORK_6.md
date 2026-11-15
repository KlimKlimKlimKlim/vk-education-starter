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

Поток данных: `LocalAppsDataSource → AppDto → AppMapper → AppInfo / AppDetails → ViewModel → Compose`. Доступ к данным выполняется через контракт `AppsRepository` и реализацию `AppsRepositoryImpl`.

- Весь хардкод приложений находится в `data/source/LocalAppsDataSource.kt`, первоначальный формат — `AppDto`.
- `AppMapper` явно преобразует DTO в две domain-модели. Реализация репозитория вызывает Mapper, а не возвращает DTO наружу.
- Контракт `AppsRepository` находится в Domain, реализация — в Data.
- Domain не зависит от Android, Compose, Data и Presentation: ни `R`, ни `DrawableRes`, ни DTO в domain-моделях нет.
- Presentation получает данные через Domain. Карточка и витрина также получают данные через ViewModel, а не обращаются к статическому источнику напрямую.
- Зависимости передаются в конструкторы. `di/AppContainer` связывает реализации с фабриками ViewModel; UI не создаёт репозиторий.
- Android-ресурсы разрешаются по строковым ключам в Presentation.
- Данные локальные и синхронные; сеть, БД, DI-фреймворк, дополнительные Gradle-модули и новые зависимости не добавлялись.
- Карточка из задания 4, 3 страницы витрины по 3 приложения, навигация и обработка Snackbar из задания 5 перенесены в Presentation.

## Запуск

```powershell
git fetch origin
git switch homework-6-clean-architecture
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest
```

Далее запустить `app` на телефоне через Android Studio.

## Сценарии на телефоне

1. Запустить приложение: проверить акцентный toolbar и список из 9 приложений с иконкой, названием, описанием и категорией.
2. Повернуть экран: контент должен сохраниться.
3. Нажать toolbar: должен появиться Snackbar «Это список приложений»; повернуть экран — сообщение не должно появиться повторно.
4. Нажать «Заметки»: проверить, что карточка показывает данные выбранного приложения.
5. Прокрутить до «Витрины»: проверить 3 страницы по 3 приложения. Нажатие должно вернуть к списку.
6. Повторить с другим приложением, проверить кнопку «Назад».
7. Отключить интернет: список, карточка и витрина должны продолжать работать с локальными данными и изображениями.

## Автотесты

- `AppMapperTest` — проверка преобразования всех полей DTO в обе domain-модели.
- `AppsRepositoryTest` — проверка подставного DTO-источника, соответствия 9 приложений карточкам и отклонения неизвестного ID.
- `AppListViewModelTest` — проверка однократного чтения списка, сохранения контента при нажатии toolbar и отсутствия replay события.
- `AppDetailsViewModelTest` — проверка однократного чтения карточки и витрины через domain-контракт.
- Существующие UI-тесты задания 4–5 оставлены без изменения сценариев.

Команды для проверки в среде с JDK и Android SDK:

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:assembleDebugAndroidTest :app:lintDebug
.\gradlew.bat :app:connectedDebugAndroidTest
```

Последняя команда требует подключённого устройства или запущенного эмулятора.

## Статус проверки публикации

- Файлы опубликованной ветки сверены с подготовленным решением.
- Выполнена статическая проверка: в Domain нет Android- и Data-импортов, в Presentation нет импортов Data или обращений к SampleApps; старые пакеты удалены.
- `git diff --check` не выявил ошибок форматирования изменений.
- В текущей среде нет JDK и Android SDK. Сборка, unit-тесты, Android UI-тесты и Lint в этой сессии не запускались; успешное прохождение не подтверждено.
- Проверка на телефоне или эмуляторе не выполнялась.
