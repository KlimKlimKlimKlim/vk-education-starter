package ru.klimtrofimov.vkeducationstarter

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.klimtrofimov.vkeducationstarter.data.network.AppsApi

class AppsApiTest {
    private lateinit var server: MockWebServer
    private lateinit var api: AppsApi

    @Before
    fun setUp() {
        server = MockWebServer().apply { start() }
        api = Retrofit.Builder().baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(AppsApi::class.java)
    }

    @After
    fun tearDown() = server.shutdown()

    @Test
    fun catalogUsesGetAndParsesRealResponseFields() = runTest {
        server.enqueue(jsonResponse("catalog.json"))
        val apps = api.getCatalog()
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/catalog", request.path)
        assertEquals(2, apps.size)
        assertEquals("QuickNote", apps.first().name)
        assertEquals("Производительность", apps.first().category)
        assertTrue(apps.first().iconUrl.startsWith("https://"))
    }

    @Test
    fun detailsUsesIdPathFromAssignmentAndParsesDetailedDto() = runTest {
        server.enqueue(jsonResponse("details.json"))
        val id = "fa2e31b8-1234-4cf7-9914-108a170a1b01"
        val details = api.getAppDetails(id)
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/$id", request.path)
        assertEquals(id, details.id)
        assertEquals("QuickSoft Inc.", details.developer)
        assertEquals(8.2f, details.size, 0.001f)
        assertEquals(5, details.screenshotUrls!!.size)
    }

    @Test
    fun notFoundIsReportedInsteadOfSilentlyChangingEndpoint() = runTest {
        server.enqueue(MockResponse().setResponseCode(404))
        try {
            api.getAppDetails("missing")
            throw AssertionError("Expected HTTP 404")
        } catch (error: HttpException) {
            assertEquals(404, error.code())
        }
        assertEquals("/missing", server.takeRequest().path)
        assertEquals(1, server.requestCount)
    }

    private fun jsonResponse(name: String) = MockResponse()
        .setHeader("Content-Type", "application/json")
        .setBody(checkNotNull(javaClass.getResource("/$name")).readText())
}
