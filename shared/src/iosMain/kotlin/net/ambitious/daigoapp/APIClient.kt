package net.ambitious.daigoapp

import io.ktor.client.*
import io.ktor.client.engine.ios.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_current_queue
import kotlin.coroutines.CoroutineContext

actual class APIClient actual constructor() {
  actual val noAuthClient: HttpClient = projectHttpClient(Ios, false)
  actual val authClient: HttpClient = projectHttpClient(Ios, true)

  actual val dispatcher: CoroutineDispatcher = object : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
      dispatch_async(dispatch_get_current_queue()) {
        block.run()
      }
    }
  }
}