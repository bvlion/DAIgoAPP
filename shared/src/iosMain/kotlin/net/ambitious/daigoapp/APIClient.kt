package net.ambitious.daigoapp

import io.ktor.client.*
import io.ktor.client.engine.ios.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_current_queue
import kotlin.coroutines.CoroutineContext

actual class APIClient actual constructor() {
  actual val client: HttpClient = HttpClient(Ios) {
    install(JsonFeature) {
      serializer = KotlinxSerializer(json = kotlinx.serialization.json.Json {
        isLenient = false
        ignoreUnknownKeys = true
        allowSpecialFloatingPointValues = true
        useArrayPolymorphism = false
      })
    }
  }

  actual val dispatcher: CoroutineDispatcher = object : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
      dispatch_async(dispatch_get_current_queue()) {
        block.run()
      }
    }
  }
}