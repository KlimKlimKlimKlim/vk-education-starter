package ru.klimtrofimov.vkeducationstarter

import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import ru.klimtrofimov.vkeducationstarter.domain.repository.AppsRepository
import ru.klimtrofimov.vkeducationstarter.domain.usecase.LoadRatingsUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class LoadRatingsUseCaseTest {
    private val ids = List(12) { "app-$it" }

    @Test
    fun sequentialTakesSumOfDelaysAndParallelTakesOneDelay() = runTest {
        val repository = FakeAppsRepository()
        val loader = LoadRatingsUseCase(repository)
        val start = testScheduler.currentTime
        val sequential = loader.loadRatingsSequentially(ids)
        assertEquals(18_000L, testScheduler.currentTime - start)
        val parallelStart = testScheduler.currentTime
        val parallel = loader.loadRatingsInParallel(ids)
        assertEquals(1_500L, testScheduler.currentTime - parallelStart)
        assertEquals(ids.toSet(), sequential.keys)
        assertEquals(sequential, parallel)
    }

    @Test
    fun parallelStartsEveryChildAndParentCancellationCancelsThemAll() = runTest {
        var active = 0
        var peak = 0
        val repository = object : AppsRepository by FakeAppsRepository() {
            override suspend fun getRating(id: String): Float {
                active++
                peak = maxOf(peak, active)
                try { delay(1500); return 4f } finally { active-- }
            }
        }
        val job = launch { LoadRatingsUseCase(repository).loadRatingsInParallel(ids) }
        runCurrent()
        assertEquals(12, active)
        assertEquals(12, peak)
        job.cancel()
        job.join()
        assertEquals(0, active)
    }

    @Test
    fun childFailureCancelsSiblingsInsteadOfLeavingBackgroundWork() = runTest {
        var active = 0
        val repository = object : AppsRepository by FakeAppsRepository() {
            override suspend fun getRating(id: String): Float {
                active++
                try {
                    if (id == ids.first()) { delay(100); throw IOException("failed") }
                    delay(1500)
                    return 4f
                } finally { active-- }
            }
        }
        try {
            LoadRatingsUseCase(repository).loadRatingsInParallel(ids)
            fail("Expected IOException")
        } catch (_: IOException) {
            assertEquals(0, active)
            assertEquals(100L, testScheduler.currentTime)
        }
    }

    @Test
    fun sequentialAlsoPropagatesCancellationAndEmptyListsAreSupported() = runTest {
        val repository = FakeAppsRepository()
        val loader = LoadRatingsUseCase(repository)
        assertTrue(loader.loadRatingsInParallel(emptyList()).isEmpty())
        assertTrue(loader.loadRatingsSequentially(emptyList()).isEmpty())
        val job = launch { loader.loadRatingsSequentially(ids) }
        runCurrent()
        assertEquals(1, repository.ratingCalls)
        job.cancel()
        job.join()
        advanceUntilIdle()
        assertEquals(1, repository.ratingCalls)
    }
}
