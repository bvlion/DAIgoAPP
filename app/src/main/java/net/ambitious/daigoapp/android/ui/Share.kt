package net.ambitious.daigoapp.android.ui

import android.content.Context
import android.content.Intent
import net.ambitious.daigoapp.android.BuildConfig

fun share(
  appName: String,
  inputText: String,
  resultText: String,
  context: Context
) {
  val sendIntent = Intent().apply {
    action = Intent.ACTION_SEND
    putExtra(
      Intent.EXTRA_TEXT,
      "$appName が「$inputText」を「$resultText」と変換しました！\nあなたも試してみよう☆\nhttps://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
    )
    type = "text/plain"
  }
  val shareIntent = Intent.createChooser(sendIntent, null)
  context.startActivity(shareIntent)
}