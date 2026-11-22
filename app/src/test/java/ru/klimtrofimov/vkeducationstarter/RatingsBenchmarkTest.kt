package ru.klimtrofimov.vkeducationstarter

import kotlin.system.measureTimeMillis
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Assume.assumeTrue
import org.junit.Test
import ru.klimtrofimov.vkeducationstarter.data.network.AppsApi
import ru.klimtrofimov.vkeducationstarter.data.dto.AppDetailsDto
import ru.klimtrofimov.vkeducationstarter.data.dto.CatalogAppDto
import ru.klimtrofimov.vkeducationstarter.domain.repository.AppsRepository
import ru.klimtrofimov.vkeducationstarter.domain.usecase.LoadRatingsUseCase

class RatingsBenchmarkTest {
    @Test
    fun compareTwelveAppsWithRealDelay() {
        assumeTrue("Set RUN_RATINGS_BENCHMARK=1 to measure real time", System.getenv("RUN_RATINGS_BENCHMARK") == "1")
        val api = object : AppsApi {
            override suspend fun getCatalog(): List<CatalogAppDto> = error("Not used")
            override suspend fun getAppDetails(id: String): AppDetailsDto = error("Not used")
        }
        val repository = object : AppsRepository by FakeAppsRepository() {
            override suspend fun getRating(id: String): Float = api.getRating(id)
        }
        val loader = LoadRatingsUseCase(repository)
        val ids = listOf(
            "fa2e31b8-1234-4cf7-9914-108a170a1b01",
            "82ea6f63-2b2a-43e1-b54c-ec62e6e27229",
            "c6f64035-c432-4aad-a550-a1dac2d3b047",
            "d22b7cda-2d36-4ebd-bfa3-be58770fa455",
            "f830e414-42a9-4fe6-af16-555a85f6f6c2",
            "ba31ce5d-b214-46e9-bf7c-35e99e70af14",
            "204a939c-39fb-4c9d-bed9-daeeba8a465e",
            "1ec98719-1d86-4cdc-b99d-11c6c23d981e",
            "d8180b87-56de-4737-b5ca-953d8c15ec80",
            "572a8cc3-662e-47b8-9f69-b8f5454415c4",
            "152b90b4-4632-4acd-b8fd-7ae71bee95e7",
            "86e9355c-1ff0-4920-8cba-7bb2ea177625",
        )
        runBlocking {
            var sequential = emptyMap<String, Float>()
            var parallel = emptyMap<String, Float>()
            val sequentialMs = measureTimeMillis { sequential = loader.loadRatingsSequentially(ids) }
            val parallelMs = measureTimeMillis { parallel = loader.loadRatingsInParallel(ids) }
            assertEquals(12, sequential.size)
            assertEquals(12, parallel.size)
            assertTrue((sequential.values + parallel.values).all { it in 3f..5f })
            println("RATINGS_BENCHMARK count=12 sequentialMs=$sequentialMs parallelMs=$parallelMs")
        }
    }
}
