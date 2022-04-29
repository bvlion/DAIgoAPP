package net.ambitious.daigoapp.call

open class Result<out R> {

  data class Success<out T>(val data: T) : Result<T>()
  data class Failure(val err: ErrorDetail) : Result<Nothing>()

  data class ErrorDetail(
    val title: String = "通信エラー",
    val message: String = "通信エラーが発生しました。\nお手数ですが再度実行してください。",
    val status: Int,
    val cause: Exception
  )

  companion object {
    fun <T> success(data: T): Result<T> {
      return Success(data)
    }

    fun <T> failure(msg: ErrorDetail): Result<T> {
      return Failure(msg)
    }
  }
}