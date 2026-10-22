# Практическое задание 5

## Реализация

- `AppListViewModel` — presentation-слой списка по MVVM.
- `StateFlow<AppListUiState>` хранит контент; репозиторий читается один раз при создании ViewModel.
- `AppListRoute` получает ViewModel и собирает state через `collectAsStateWithLifecycle`.
- `AppListScreen` — stateless UI, которому передаются состояние и callbacks.
- Клик по любой области toolbar отправляет одноразовый `AppListEffect`.
- Эффекты идут через `SharedFlow` с `replay = 0`; старый Snackbar не приходит новому collector после переворота.

## Самопроверка на телефоне

1. Запустить приложение: отображается список из девяти приложений.
2. Прокрутить список, чтобы убедиться, что контент загружен.
3. Повернуть телефон: список остается, повторной загрузки из репозитория нет.
4. Нажать toolbar: появляется Snackbar «Это список приложений».
5. Пока Snackbar виден, повернуть телефон: список остается, Snackbar не появляется повторно.
6. Нажать toolbar еще раз: появляется ровно один новый Snackbar.
7. Убедиться, что переходы на карточку приложения и обратно из задания 4 продолжают работать.

## Команды

```powershell
git fetch origin
git switch homework-5-presentation
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest
# С подключенным телефоном/эмулятором:
.\gradlew.bat :app:connectedDebugAndroidTest
```

## Автопроверки

- Unit-тест проверяет, что источник списка вызывается один раз и состояние переиспользуется.
- UI-тест нажимает toolbar, пересоздает Activity, проверяет сохранение контента и отсутствие повторного Snackbar.
