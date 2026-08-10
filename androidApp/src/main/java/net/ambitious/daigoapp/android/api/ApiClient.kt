package net.ambitious.daigoapp.android.api

import kotlinx.serialization.json.Json
import net.ambitious.daigoapp.android.BuildConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.Retrofit

object ApiClient {
  val host: String = BuildConfig.HOST.ifBlank { "http://10.0.2.2:8080" }

  val rulesUrl = "$host/app/rules?textColor=%s&backColor=%s&isPrivacyPolicy="

  private val json = Json {
    isLenient = false
    ignoreUnknownKeys = true
    allowSpecialFloatingPointValues = true
    useArrayPolymorphism = false
  }

  private val noAuthClient = OkHttpClient.Builder().build()

  private val authClient = OkHttpClient.Builder()
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
