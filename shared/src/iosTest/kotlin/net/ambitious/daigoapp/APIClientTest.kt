package net.ambitious.daigoapp

import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(DelicateCoroutinesApi::class)
@ExperimentalCoroutinesApi
actual class APIClientTest {

  private val job = SupervisorJob()
  private val scope = CoroutineScope(Dispatchers.Main + job)

  @Test
  actual fun testHost() {
    val apiClient = APIClient()
    assertEquals("http://localhost:8080", apiClient.host)
  }

  @Test // See: https://httpbin.org/
  actual fun testNoAuthClient() {
    val apiClient = APIClient()
    runBlocking(newSingleThreadContext("testRunner")) {
      val result = apiClient.noAuthClient.get<String>("http://httpbin.org/get?target=test")
      val json = Json.parseToJsonElement(result).jsonObject
      val args = json["args"]?.jsonObject
      assertEquals("test", args?.get("target")?.jsonPrimitive?.content)
    }
  }

  @Test // See: https://httpbin.org/
  actual fun testAuthClient() {
    val apiClient = APIClient()
    runBlocking(newSingleThreadContext("testRunner")) {
      val result = apiClient.authClient.get<String>("http://httpbin.org/bearer")
      val json = Json.parseToJsonElement(result).jsonObject
      assertEquals("test_test", json["token"]?.jsonPrimitive?.content)
    }
  }

  @Test
  actual fun testDispatcher() {
    val apiClient = APIClient()
    runBlocking(newSingleThreadContext("testRunner")) {
      scope.launch(newSingleThreadContext("testRunner")) {
        // same testAuthClient
        val result = apiClient.authClient.get<String>("http://httpbin.org/bearer")
        val json = Json.parseToJsonElement(result).jsonObject
        assertEquals("test_test", json["token"]?.jsonPrimitive?.content)
      }.join()
    }
    // In the UnitTest, only a null check is performed because the ThreadContext is different.
    assertNotNull(apiClient.dispatcher)
  }
}