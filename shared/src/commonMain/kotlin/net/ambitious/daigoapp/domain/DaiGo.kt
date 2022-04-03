package net.ambitious.daigoapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object DaiGo {
  @Serializable
  data class GenerateResponse(val text: String)

  @Serializable
  data class UpdateRequest(
    val word: String,
    @SerialName("dai_go") val daiGo: String
  )

  @Serializable
  data class UpdateResponse(val save: String)
}