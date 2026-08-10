package net.ambitious.daigoapp.android.api

import kotlinx.serialization.json.Json
import net.ambitious.daigoapp.android.BuildConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object ApiClient {
  internal fun resolveHost(rawHost: String) = rawHost.ifBlank { "http://10.0.2.2:8080" }

  val host: String = resolveHost(BuildConfig.HOST)

  val rulesUrl = "$host/app/rules?textColor=%s&backColor=%s&isPrivacyPolicy="

  private val json = Json {
    isLenient = false
    ignoreUnknownKeys = true
    allowSpecialFloatingPointValues = true
    useArrayPolymorphism = false
  }

  internal val noAuthClient: OkHttpClient = OkHttpClient.Builder().build()

  internal val authClient: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor { chain ->
      chain.proceed(
        chain.request().newBuilder()
          .addHeader("Authorization", "Bearer ${BuildConfig.BEARER}")
          .build()
      )
    }
    .build()

  private fun retrofit(client: OkHttpClient) = Retrofit.Builder()
    .baseUrl("$host/")
    .client(client)
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .build()

  val noAuthService: ApiService by lazy { retrofit(noAuthClient).create(ApiService::class.java) }
  val authService: ApiService by lazy { retrofit(authClient).create(ApiService::class.java) }
}
