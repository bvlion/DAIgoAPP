package net.ambitious.daigoapp.android.api

import net.ambitious.daigoapp.android.domain.DaiGo
import net.ambitious.daigoapp.android.domain.Rules
import net.ambitious.daigoapp.android.domain.Samples
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
  @GET("privacy_policy")
  suspend fun getPrivacyPolicy(): Rules

  @GET("terms_of_use")
  suspend fun getTermsOfUse(): Rules

  @GET("get-dai-go")
  suspend fun getDaigo(@Query("target") target: String): DaiGo.GenerateResponse

  @POST("upsert-dai-go")
  suspend fun postDaigo(@Body request: DaiGo.UpdateRequest): DaiGo.UpdateResponse

  @GET("get-samples")
  suspend fun getSamples(): Samples
}
