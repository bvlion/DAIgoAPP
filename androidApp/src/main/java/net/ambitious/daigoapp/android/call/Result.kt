package net.ambitious.daigoapp.android.call

import kotlinx.coroutines.CancellationException
import retrofit2.HttpException

sealed class Result<out R> {

  data class Success<out T>(val data: T) : Result<T>()
  data class Failure(val err: ErrorDetail) : Result<Nothing>()

  data class ErrorDetail(
    val title: String = "通信エラー",
    val message: String = "通信エラーが発生しました。\nお手数ですが再度実行してください。",
    val status: Int,
    val cause: Exception
  )

  companion object {
    /** Run [request] and wrap its outcome, mirroring the HTTP status / cause the caller used to receive. */
    suspend fun <T> of(request: suspend () -> T): Result<T> = try {
      Success(request())
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      Failure(ErrorDetail(
        status = (e as? HttpException)?.code() ?: -1,
        cause = e
      ))
    }
  }
}
