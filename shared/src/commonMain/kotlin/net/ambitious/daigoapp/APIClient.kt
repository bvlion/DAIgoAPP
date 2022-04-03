package net.ambitious.daigoapp

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import net.ambitious.daigoapp.BuildKonfig.bearer

expect class APIClient() {
  val noAuthClient: HttpClient
  val authClient: HttpClient
  val dispatcher: CoroutineDispatcher
}

fun <T : HttpClientEngineConfig> projectHttpClient(
  engineFactory: HttpClientEngineFactory<T>,
  useAuth: Boolean
) = HttpClient(engineFactory) {
  install(JsonFeature) {
    serializer = KotlinxSerializer(json = kotlinx.serialization.json.Json {
      isLenient = false
      ignoreUnknownKeys = true
      allowSpecialFloatingPointValues = true
      useArrayPolymorphism = false
    })
  }

  defaultRequest {
    if (useAuth) {
      header(HttpHeaders.Authorization, "Bearer $bearer")
    }
  }
}