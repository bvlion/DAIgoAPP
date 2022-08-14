package net.ambitious.daigoapp

import io.ktor.client.features.*
import kotlinx.coroutines.launch
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import net.ambitious.daigoapp.call.Result
import net.ambitious.daigoapp.domain.DaiGo
import net.ambitious.daigoapp.domain.Rules
import net.ambitious.daigoapp.domain.Samples

class API {
  private val apiClient = APIClient()

  private val job = SupervisorJob()
  private val scope = CoroutineScope(Dispatchers.Main + job)

  val rulesUrl = "${apiClient.host}/app/rules?textColor=%s&backColor=%s&isPrivacyPolicy="

  fun cancel() = job.cancel("cancel")

  fun getRules(isPrivacyPolicy: Boolean, callback: (Result<Rules>) -> Unit) {
    val url = apiClient.host + if (isPrivacyPolicy) {
      "/privacy_policy"
    } else {
      "/terms_of_use"
    }

    call({ apiClient.noAuthClient.get(url) }, callback)
  }

  fun getDaigo(target: String, callback: (Result<DaiGo.GenerateResponse>) -> Unit) =
    call({ apiClient.authClient.get("${apiClient.host}/get-dai-go") {
      parameter("target", target)
    }}, callback)

  fun postDaigo(word: String, daiGo: String, callback: (Result<DaiGo.UpdateResponse>) -> Unit) =
    call({ apiClient.authClient.post("${apiClient.host}/upsert-dai-go") {
      body = DaiGo.UpdateRequest(word, daiGo)
      contentType(ContentType.Application.Json)
    }}, callback)

  fun getSamples(callback: (Result<Samples>) -> Unit) =
    call({ apiClient.authClient.get("${apiClient.host}/get-samples") }, callback)

  fun <T> call(
    request: suspend () -> T,
    callback: (Result<T>) -> Unit,
    dispatcher: CoroutineDispatcher = apiClient.dispatcher
  ): Job =
    scope.launch(dispatcher) {
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