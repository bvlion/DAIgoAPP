package net.ambitious.daigoapp

import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import net.ambitious.daigoapp.call.Result
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class APITest {

  private val apiClient = APIClient()

  @Test // See: https://httpbin.org/
  fun errorResponseTest() =
    testCall({ apiClient.noAuthClient.get<String>("http://httpbin.org/status/400") }) {
      if (it !is Result.Failure) {
        throw IllegalStateException("Result is not Failure")
      }
      assertEquals(400, it.err.status)
    }

  @Test // See: https://httpbin.org/
  fun successResponseTest() =
    testCall({ apiClient.authClient.get<String>("http://httpbin.org/bearer") }) {
      if (it !is Result.Success) {
        throw IllegalStateException("Result is not Success")
      }
      val json = Json.parseToJsonElement(it.data).jsonObject
      assertEquals("test_test", json["token"]?.jsonPrimitive?.content)
    }

  private fun <T> testCall(
    request: suspend () -> T,
    test: (Result<T>) -> Unit
  ) = runBlocking(newSingleThreadContext("testRunner")) {
    API().call(request, test, newSingleThreadContext("testRunner")).join()
  }
}