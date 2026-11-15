package ru.klimtrofimov.vkeducationstarter.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import ru.klimtrofimov.vkeducationstarter.domain.repository.AppsRepository

class LoadRatingsUseCase @Inject constructor(private val repository: AppsRepository) {
    suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float> =
        ids.associateWith { id -> repository.getRating(id) }

    suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float> = coroutineScope {
        ids.map { id -> async { id to repository.getRating(id) } }.awaitAll().toMap()
    }
}
