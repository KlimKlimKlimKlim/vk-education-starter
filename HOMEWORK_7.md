# Практическое задание 7 — внедрение зависимостей

Решение основано на ветке `homework-6-clean-architecture`. Ветка задания 7 —
`homework-7-hilt`, PR направляется в `main` и не сливается автоматически.

## Выполнено

- Подключены Hilt 2.56.2 и KSP 2.0.21-1.0.28 для существующего Kotlin 2.0.21.
- `VkEducationApplication` помечен `@HiltAndroidApp` и зарегистрирован в манифесте.
- `MainActivity` помечена `@AndroidEntryPoint`.
- `AppsModule` установлен в `SingletonComponent`. Метод
  `@Binds bindAppsRepository(impl: AppsRepositoryImpl): AppsRepository`
  связывает интерфейс с реализацией; второй `@Binds` связывает `AppsDataSource`
  с `LocalAppsDataSource`. Обе привязки имеют `@Singleton`.
- `AppsRepositoryImpl`, `AppMapper` и `LocalAppsDataSource` используют
  `@Inject constructor`.
- `AppListViewModel` и `AppDetailsViewModel` помечены `@HiltViewModel` и получают
  интерфейс `AppsRepository` через `@Inject constructor`.
- Compose-маршруты получают ViewModel через `hiltViewModel()`, в области
  соответствующего `NavBackStackEntry`. В карточке `appId` берётся из
  автоматически внедряемого `SavedStateHandle`.
- Ручной `AppContainer` и передача фабрик ViewModel удалены.
- Domain остаётся независимым от Android и DI; прежние UI и навигация сохранены.

Граф зависимостей:

```text
AppListViewModel / AppDetailsViewModel
    -> AppsRepository (@Binds)
        -> AppsRepositoryImpl (@Inject)
            -> AppsDataSource (@Binds)
                -> LocalAppsDataSource (@Inject)
            -> AppMapper (@Inject)
```

## Запуск и самопроверка

```powershell
git fetch origin
git switch homework-7-hilt
```

Откройте проект в Android Studio, выполните Gradle Sync и Run. Для Gradle
используйте JDK 17 или более новый совместимый JDK из Android Studio;
потребуются Android SDK 36 и подключение к интернету для загрузки зависимостей.

1. Приложение запускается без ошибок создания Hilt-компонента или ViewModel;
   в списке девять приложений.
2. Откройте «Заметки», затем другое приложение: карточка должна соответствовать
   выбранному элементу. Так проверяется передача `appId` через `SavedStateHandle`.
3. Поверните телефон на экране списка и на карточке: содержимое сохраняется.
4. Нажмите toolbar списка: появится Snackbar. При повороте экрана старый Snackbar
   не должен показываться повторно.
5. Внизу карточки проверьте витрину: три страницы по три приложения; клик возвращает
   к списку.

## Автоматические проверки

Из корня проекта в PowerShell:

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:assembleDebugAndroidTest :app:lintDebug
```

С подключённым телефоном или эмулятором:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest
```

Тест `AppDetailsViewModelTest` обновлён для `SavedStateHandle`. Добавлен
`HiltViewModelTest`: ViewModel создаётся стандартным `ViewModelProvider` Activity
с фабрикой Hilt, получает данные через реальный DI-граф и сохраняется после
пересоздания Activity. Существующие UI-тесты проверяют навигацию и Snackbar.

Статус подготовки: проверены конфигурация зависимостей, привязки Hilt, отсутствие
ручного контейнера и `git diff --check`. В среде подготовки нет JDK и Android SDK,
поэтому сборка, unit-/UI-тесты и Lint **не запускались**; успешное прохождение
этих проверок не заявляется.
