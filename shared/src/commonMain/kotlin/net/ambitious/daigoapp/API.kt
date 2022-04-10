package net.ambitious.daigoapp

import kotlinx.coroutines.launch
import io.ktor.client.request.*
import kotlinx.coroutines.*
import net.ambitious.daigoapp.domain.DaiGo
import net.ambitious.daigoapp.domain.Rules
import net.ambitious.daigoapp.domain.Samples

class API {
  private val apiClient = APIClient()

  private val job = SupervisorJob()
  private val scope = CoroutineScope(Dispatchers.Main + job)

  fun cancel() = job.cancel("cancel")

  fun getRules(isPrivacyPolicy: Boolean, callback: (Rules) -> Unit) {
    val url = apiClient.host + if (isPrivacyPolicy) {
      "/privacy_policy"
    } else {
      "/terms_of_use"
    }

    scope.launch(apiClient.dispatcher) {
      val result = apiClient.noAuthClient.get<Rules>(url)
      callback(result)
    }
  }

  fun getDaigo(target: String, callback: (DaiGo.GenerateResponse) -> Unit) {
    scope.launch(apiClient.dispatcher) {
      val result = apiClient.authClient.get<DaiGo.GenerateResponse>("${apiClient.host}/get-dai-go") {
        parameter("target", target)
      }
      callback(result)
    }
  }

  fun postDaigo(word: String, daiGo: String, callback: (DaiGo.UpdateResponse) -> Unit) {
    scope.launch(apiClient.dispatcher) {
      val result = apiClient.authClient.post<DaiGo.UpdateResponse>("${apiClient.host}/upsert-dai-go") {
        body = DaiGo.UpdateRequest(word, daiGo)
      }
      callback(result)
    }
  }

  fun getSamples(callback: (Samples) -> Unit) {
    scope.launch(apiClient.dispatcher) {
      val result = apiClient.authClient.get<Samples>("${apiClient.host}/get-samples")
      callback(result)
    }
  }
}