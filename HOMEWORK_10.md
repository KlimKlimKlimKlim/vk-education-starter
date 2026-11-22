# Практическое задание 10 — Unit-тесты

Новые тесты: `app/src/test/java/ru/klimtrofimov/vkeducationstarter/unit/`

| Слой | Файл | Тестов |
|---|---|---:|
| Data: Repository | `AppsRepositoryImplUnitTest.kt` | 8 |
| Data: Mapper (DTO → Domain и Domain → DTO) | `AppMapperUnitTest.kt` | 9 |
| Domain: UseCase | `LoadRatingsUseCaseUnitTest.kt` | 7 |

Для обратного направления в `AppMapper` добавлен `toCatalogDto(AppInfo)`.

Запуск: `./gradlew :app:testDebugUnitTest`
