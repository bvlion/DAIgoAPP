package net.ambitious.daigoapp.android.api

import net.ambitious.daigoapp.android.BuildConfig
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ApiClientTest {

  private lateinit var server: MockWebServer

  @Before
  fun setUp() {
    server = MockWebServer()
    server.start()
  }

  @After
  fun tearDown() {
    server.shutdown()
  }

  @Test
  fun resolveHostFallsBackToEmulatorHostWhenBlank() {
    assertEquals("http://10.0.2.2:8080", ApiClient.resolveHost(""))
    assertEquals("http://10.0.2.2:8080", ApiClient.resolveHost("   "))
  }

  @Test
  fun resolveHostKeepsConfiguredHostWhenPresent() {
    assertEquals("https://example.com", ApiClient.resolveHost("https://example.com"))
  }

  @Test
  fun authClientAddsBearerAuthorizationHeader() {
    server.enqueue(MockResponse().setBody("ok"))

    ApiClient.authClient.newCall(Request.Builder().url(server.url("/")).build()).execute().close()

    val request = server.takeRequest()
    assertEquals("Bearer ${BuildConfig.BEARER}", request.getHeader("Authorization"))
  }

  @Test
  fun noAuthClientDoesNotAddAuthorizationHeader() {
    server.enqueue(MockResponse().setBody("ok"))

    ApiClient.noAuthClient.newCall(Request.Builder().url(server.url("/")).build()).execute().close()

    val request = server.takeRequest()
    assertNull(request.getHeader("Authorization"))
  }
}
