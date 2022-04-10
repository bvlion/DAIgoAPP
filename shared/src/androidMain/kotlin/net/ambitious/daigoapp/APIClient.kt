package net.ambitious.daigoapp

import io.ktor.client.*
import io.ktor.client.engine.android.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual class APIClient actual constructor() {
  actual val noAuthClient: HttpClient = projectHttpClient(Android, false)
  actual val authClient: HttpClient = projectHttpClient(Android, true)
  actual val dispatcher: CoroutineDispatcher = Dispatchers.Default
  actual val host = BuildKonfig.host.ifBlank {
    "http://10.0.2.2:8080"
  }
}