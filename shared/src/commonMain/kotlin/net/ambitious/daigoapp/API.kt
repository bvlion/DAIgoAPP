package net.ambitious.daigoapp

import io.ktor.client.features.*
import kotlinx.coroutines.launch
import io.ktor.client.request.*
import kotlinx.coroutines.*
import net.ambitious.daigoapp.call.Result
import net.ambitious.daigoapp.domain.DaiGo
import net.ambitious.daigoapp.domain.Rules
import net.ambitious.daigoapp.domain.Samples

class API {
  private val apiClient = APIClient()

  private val job = SupervisorJob()
  private val scope = CoroutineScope(Dispatchers.Main + job)

  fun cancel() = job.cancel("cancel")

  fun getRules(isPrivacyPolicy: Boolean, callback: (Result<Rules>) -> Unit) {
    val url = apiClient.host + if (isPrivacyPolicy) {
      "/privacy_policy"
    } else {
      "/terms_of_use"
    }

    call({ apiClient.noAuthClient.get<Rules>(url) }) {
      callback(it)
    }
  }

  fun getDaigo(target: String, callback: (Result<DaiGo.GenerateResponse>) -> Unit) =
    call({ apiClient.authClient.get<DaiGo.GenerateResponse>("${apiClient.host}/get-dai-go") {
      parameter("target", target)
    }}) {
      callback(it)
    }

  fun postDaigo(word: String, daiGo: String, callback: (Result<DaiGo.UpdateResponse>) -> Unit) =
    call({ apiClient.authClient.post<DaiGo.UpdateResponse>("${apiClient.host}/upsert-dai-go") }) {
      callback(it)
    }

  fun getSamples(callback: (Result<Samples>) -> Unit) =
    call({ apiClient.authClient.get<Samples>("${apiClient.host}/get-samples") }) {
      callback(it)
    }

  fun <T> call(request: suspend () -> T, callback: (Result<T>) -> Unit): Job =
    scope.launch(apiClient.dispatcher) {
      try {
        callback(Result.success(request()))
      } catch (e: Exception) {
        val status = if (e is ClientRequestException) {
          e.response.status.value
        } else {
          -1
        }
        callback(Result.failure(Result.ErrorDetail(
          status = status,
          cause = e
        )))
      }
    }
}