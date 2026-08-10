package net.ambitious.daigoapp.android.api

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import net.ambitious.daigoapp.android.domain.DaiGo
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class ApiServiceTest {

  private lateinit var server: MockWebServer
  private lateinit var service: ApiService

  @Before
  fun setUp() {
    server = MockWebServer()
    server.start()

    val json = Json { ignoreUnknownKeys = true }
    service = Retrofit.Builder()
      .baseUrl(server.url("/"))
      .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
      .build()
      .create(ApiService::class.java)
  }

  @After
  fun tearDown() {
    server.shutdown()
  }

  @Test
  fun getDaigoSendsTargetQueryAndParsesResponse() = runBlocking {
    server.enqueue(MockResponse().setBody("""{"text":"JSSK"}"""))

    val response = service.getDaigo("上昇志向")

    assertEquals("JSSK", response.text)
    val request = server.takeRequest()
    assertEquals("上昇志向", request.requestUrl?.queryParameter("target"))
  }

  @Test
  fun postDaigoSendsSerializedBodyAndParsesResponse() = runBlocking {
    server.enqueue(MockResponse().setBody("""{"save":"ok"}"""))

    val response = service.postDaigo(DaiGo.UpdateRequest("結婚してください", "KSK"))

    assertEquals("ok", response.save)
    val request = server.takeRequest()
    assertEquals("POST", request.method)
    assertEquals(
      """{"word":"結婚してください","dai_go":"KSK"}""",
      request.body.readUtf8()
    )
  }

  @Test
  fun getSamplesParsesListResponse() = runBlocking {
    server.enqueue(MockResponse().setBody("""{"samples":["JSSK","KSK"]}"""))

    val response = service.getSamples()

    assertEquals(listOf("JSSK", "KSK"), response.samples)
  }

  @Test(expected = HttpException::class)
  fun errorResponseThrowsHttpException(): Unit = runBlocking {
    server.enqueue(MockResponse().setResponseCode(400))
    service.getSamples()
  }
}
