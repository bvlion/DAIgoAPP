package net.ambitious.daigoapp.android.call

import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ResultTest {

  @Test
  fun successWrapsData() = runBlocking {
    val result = Result.of { "ok" }

    assertTrue(result is Result.Success)
    assertEquals("ok", (result as Result.Success).data)
  }

  @Test
  fun httpErrorKeepsStatusAndCause() = runBlocking {
    val response = Response.error<String>(
      404,
      "not found".toResponseBody("text/plain".toMediaType())
    )

    val result = Result.of<String> { throw HttpException(response) }

    assertTrue(result is Result.Failure)
    val err = (result as Result.Failure).err
    assertEquals(404, err.status)
    assertTrue(err.cause is HttpException)
  }

  @Test
  fun nonHttpErrorFallsBackToUnknownStatus() = runBlocking {
    val result = Result.of<String> { throw IOException("network down") }

    assertTrue(result is Result.Failure)
    assertEquals(-1, (result as Result.Failure).err.status)
  }
}
