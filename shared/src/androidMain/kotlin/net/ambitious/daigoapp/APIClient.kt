package net.ambitious.daigoapp

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual class APIClient actual constructor() {
  actual val client: HttpClient = HttpClient(Android) {
    install(JsonFeature) {
      serializer = KotlinxSerializer(json = kotlinx.serialization.json.Json {
        isLenient = false
        ignoreUnknownKeys = true
        allowSpecialFloatingPointValues = true
        useArrayPolymorphism = false
      })
    }
  }
  actual val dispatcher: CoroutineDispatcher = Dispatchers.Default
}