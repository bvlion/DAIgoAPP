package net.ambitious.daigoapp

import io.ktor.client.*
import kotlinx.coroutines.CoroutineDispatcher

expect class APIClient() {
  val client: HttpClient
  val dispatcher: CoroutineDispatcher
}