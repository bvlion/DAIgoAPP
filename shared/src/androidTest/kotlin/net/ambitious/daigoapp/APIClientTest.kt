package net.ambitious.daigoapp

import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Test
import kotlin.test.assertEquals

actual class APIClientTest {

  private val job = SupervisorJob()
  private val scope = CoroutineScope(Dispatchers.Main + job)

  @Test
  actual fun testHost() {
    val apiClient = APIClient()
    assertEquals("http://10.0.2.2:8080", apiClient.host)
  }

  @Test // See: https://httpbin.org/
  actual fun testNoAuthClient() {
    val apiClient = APIClient()
    runBlocking {
      val result = apiClient.noAuthClient.get<String>("https://httpbin.org/get?target=test")
      val json = Json.parseToJsonElement(result).jsonObject
      val args = json["args"]?.jsonObject
      assertEquals("test", args?.get("target")?.jsonPrimitive?.content)
    }
  }

  @Test // See: https://httpbin.org/
  actual fun testAuthClient() {
    val apiClient = APIClient()
    runBlocking {
      val result = apiClient.authClient.get<String>("https://httpbin.org/bearer")
      val json = Json.parseToJsonElement(result).jsonObject
      assertEquals("test_test", json["token"]?.jsonPrimitive?.content)
    }
  }

  @Test
  actual fun testDispatcher() {
    val apiClient = APIClient()
    runBlocking {
      scope.launch(apiClient.dispatcher) {
        // same testAuthClient
        val result = apiClient.authClient.get<String>("https://httpbin.org/bearer")
        val json = Json.parseToJsonElement(result).jsonObject
        assertEquals("test_test", json["token"]?.jsonPrimitive?.content)
      }.join()
    }
  }
}