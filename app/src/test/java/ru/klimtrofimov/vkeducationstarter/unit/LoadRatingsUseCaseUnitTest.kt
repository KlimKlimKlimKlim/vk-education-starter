package ru.klimtrofimov.vkeducationstarter.unit

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.klimtrofimov.vkeducationstarter.domain.model.AppDetails
import ru.klimtrofimov.vkeducationstarter.domain.model.AppInfo
import ru.klimtrofimov.vkeducationstarter.domain.repository.AppsRepository
import ru.klimtrofimov.vkeducationstarter.domain.usecase.LoadRatingsUseCase

class LoadRatingsUseCaseUnitTest {
    private class RatingRepo(private val failId: String? = null) : AppsRepository {
        val calls = mutableListOf<String>()
        override suspend fun getRating(id: String): Float {
            delay(1000)
            calls += id
            if (id == failId) error("fail $id")
            return id.length.toFloat()
        }
        override suspend fun getApps(): List<AppInfo> = emptyList()
        override fun getCachedApps(): List<AppInfo> = emptyList()
        override fun getAppDetails(id: String): AppDetails = error("unused")
        override suspend fun getRemoteAppDetails(id: String): AppDetails = error("unused")
    }

    private val ids = listOf("a", "bb", "ccc")

    @Test fun sequentialReturnsRatingForEveryId() = runTest {
        assertEquals(mapOf("a" to 1f, "bb" to 2f, "ccc" to 3f), LoadRatingsUseCase(RatingRepo()).loadRatingsSequentially(ids))
    }

    @Test fun sequentialKeepsOrderAndTakesSumOfDelays() = runTest {
        val repo = RatingRepo()
        LoadRatingsUseCase(repo).loadRatingsSequentially(ids)
        assertEquals(ids, repo.calls)
        assertEquals(3000L, currentTime)
    }

    @Test fun parallelReturnsSameRatings() = runTest {
        val uc = LoadRatingsUseCase(RatingRepo())
        assertEquals(uc.loadRatingsSequentially(ids), uc.loadRatingsInParallel(ids))
    }

    @Test fun parallelTakesSingleDelay() = runTest {
        LoadRatingsUseCase(RatingRepo()).loadRatingsInParallel(ids)
        assertEquals(1000L, currentTime)
    }

    @Test fun emptyIdsGiveEmptyMap() = runTest {
        val uc = LoadRatingsUseCase(RatingRepo())
        assertTrue(uc.loadRatingsSequentially(emptyList()).isEmpty())
        assertTrue(uc.loadRatingsInParallel(emptyList()).isEmpty())
    }

    @Test(expected = IllegalStateException::class)
    fun parallelPropagatesFailure() = runTest {
        LoadRatingsUseCase(RatingRepo(failId = "bb")).loadRatingsInParallel(ids)
    }

    @Test fun sequentialStopsAfterFailure() = runTest {
        val repo = RatingRepo(failId = "a")
        runCatching { LoadRatingsUseCase(repo).loadRatingsSequentially(ids) }
        assertEquals(listOf("a"), repo.calls)
    }
}
