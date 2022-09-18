package net.ambitious.daigoapp.android.compose

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.flowlayout.FlowRow
import net.ambitious.daigoapp.android.BuildConfig
import net.ambitious.daigoapp.android.R
import net.ambitious.daigoapp.android.data.AppDataStore
import net.ambitious.daigoapp.android.data.History
import net.ambitious.daigoapp.android.ui.AppTheme
import net.ambitious.daigoapp.android.ui.noRippleClickable
import net.ambitious.daigoapp.android.ui.share
import java.net.URLEncoder
import java.util.*

@Composable
fun MenuCompose(
  viewMode: AppDataStore.ViewMode,
  saveViewMode: (AppDataStore.ViewMode) -> Unit,
  showRules: (Boolean) -> Unit,
  showHistory: () -> Unit,
) {
  val context = LocalContext.current
  Column(
    Modifier
      .fillMaxWidth()
      .padding(16.dp)) {
    Column(Modifier.padding(top = 8.dp)) {
      Text(
        fontSize = 14.sp,
        text = "Color"
      )
      Row {
        AppDataStore.ViewMode.values().forEach { mode ->
          RadioButton(
            selected = (mode == viewMode),
            onClick = { saveViewMode(mode) }
          )
          Text(
            modifier = Modifier
              .padding(top = 14.dp)
              .noRippleClickable { saveViewMode(mode) },
            fontSize = 15.sp,
            style = MaterialTheme.typography.body1.merge(),
            text = when (mode) {
              AppDataStore.ViewMode.DEFAULT -> "デフォルト"
              AppDataStore.ViewMode.LIGHT -> "ライト"
              AppDataStore.ViewMode.DARK -> "ダーク"
            }
          )
        }
      }
    }

    TextButton(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.Start),
      onClick = { showHistory() }
    ) {
      Text(modifier = Modifier.fillMaxWidth(), text = "履歴")
    }

    TextButton(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.Start),
      onClick = { showRules(false) }
    ) {
      Text(modifier = Modifier.fillMaxWidth(), text = "利用規約")
    }

    TextButton(
      modifier = Modifier.fillMaxWidth(),
      onClick = { showRules(true) }
    ) {
      Text(modifier = Modifier.fillMaxWidth(), text = "プライバシーポリシー")
    }

    TextButton(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.Start),
      onClick = {
        context.startActivity(Intent(
          Intent.ACTION_VIEW,
          Uri.parse(try {
            "market://details?id=${BuildConfig.APPLICATION_ID}"
          } catch (_: ActivityNotFoundException) {
            "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
          })
        ))
      }
    ) {
      Text(modifier = Modifier.fillMaxWidth(), text = "レビューする")
    }

    TextButton(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.Start),
      onClick = {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
          "https://docs.google.com/forms/d/e/1FAIpQLSecLnJGQu3C-24UMTcji_jnt7kRqgtTjQglKyA8xu6ILdnrDQ/viewform"
        )))
      }
    ) {
      Text(modifier = Modifier.fillMaxWidth(), text = "ご意見")
    }
  }
}

@ExperimentalComposeUiApi
@Composable
fun RulesDialogCompose(
  url: String,
  dismissClick: () -> Unit = {}
) {
  if (url.isNotEmpty()) {
    val loading = remember { mutableStateOf(true) }
    val textColor = URLEncoder.encode(
      String.format("#%06X", 0xFFFFFF and MaterialTheme.colors.onBackground.toArgb()),
      "UTF-8"
    )
    val backgroundColor = URLEncoder.encode(
      String.format("#%06X", 0xFFFFFF and MaterialTheme.colors.background.toArgb()),
      "UTF-8"
    )
    AlertDialog(
      onDismissRequest = dismissClick,
      text = {
        Box(
          Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          if (loading.value) {
            CircularProgressIndicator()
          }
          AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = ::WebView,
            update = {
              it.setBackgroundColor(Color.TRANSPARENT)
              it.loadUrl(String.format(url, textColor, backgroundColor))
              it.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                  super.onPageStarted(view, url, favicon)
                  loading.value = false
                }
              }
            }
          )
        }
      },
      properties = DialogProperties(usePlatformDefaultWidth = false),
      confirmButton = {
        TextButton(dismissClick) { Text("閉じる") }
      },
      shape = RoundedCornerShape(8.dp),
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    )
  }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun HistoryDialogCompose(
  histories: List<History>?,
  dismissClick: () -> Unit = {}
) {
  if (histories != null) {
    Dialog(
      onDismissRequest = dismissClick,
      content = {
        Surface(
          modifier = Modifier.padding(0.dp, 16.dp),
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colors.surface,
          contentColor = contentColorFor(MaterialTheme.colors.surface)
        ) {
          Column {
            Text(
              "履歴",
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(16.dp, 12.dp)
            )

            if (histories.isEmpty()) {
              Text(
                text = "履歴はありません",
                fontSize = 14.sp,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(start = 24.dp)
              )
            } else {
              Column(
                Modifier
                  .weight(1f, false)
                  .verticalScroll(rememberScrollState())
                  .padding(8.dp, 0.dp)
              ) {
                val context = LocalContext.current
                val appName = stringResource(R.string.app_name)
                val formatter = SimpleDateFormat("M月d日 HH:mm")
                histories.forEach {
                  Box(
                    Modifier
                      .clip(RoundedCornerShape(8.dp))
                      .clickable {
                        share(appName, it.text, it.abbreviation, context)
                      }
                  ) {
                    Row(
                      Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 8.dp)
                    ) {
                      Column(Modifier.weight(1f)) {
                        Text(
                          text = it.text,
                          fontSize = 15.sp
                        )
                        Text(
                          text = formatter.format(Date(it.createdAt)),
                          fontSize = 11.sp,
                          modifier = Modifier.padding(top = 4.dp)
                        )
                      }
                      Text(
                        text = it.abbreviation,
                        fontSize = 18.sp,
                        modifier = Modifier
                          .fillMaxWidth()
                          .padding(top = 8.dp, start = 4.dp)
                          .wrapContentWidth(Alignment.End)
                          .weight(0.5f)
                      )
                    }
                  }
                }
              }
            }

            FlowRow(
              Modifier
                .align(Alignment.End)
                .padding(4.dp, 2.dp)
            ) {
              TextButton(dismissClick) { Text("閉じる") }
            }
          }
        }
      }
    )
  }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@ExperimentalComposeUiApi
@Composable
fun MenuPreview() {
  AppTheme {
    HistoryDialogCompose(listOf(
      History(0, "上昇志向", "JSSK"),
      History(0, "結婚してください", "KSK"),
      History(0, "これからやろうと思ってたのにな〜やる気無くなっちゃったな〜", "KYOYNN"),
      History(0, "上昇志向", "JSSK"),
      History(0, "結婚してください", "KSK"),
      History(0, "これからやろうと思ってたのにな〜やる気無くなっちゃったな〜", "KYOYNN"),
      History(0, "上昇志向", "JSSK"),
      History(0, "結婚してください", "KSK"),
      History(0, "これからやろうと思ってたのにな〜やる気無くなっちゃったな〜", "KYOYNN"),
      History(0, "上昇志向", "JSSK"),
      History(0, "結婚してください", "KSK"),
      History(0, "これからやろうと思ってたのにな〜やる気無くなっちゃったな〜", "KYOYNN"),
      History(0, "上昇志向", "JSSK"),
      History(0, "結婚してください", "KSK"),
      History(0, "これからやろうと思ってたのにな〜やる気無くなっちゃったな〜", "KYOYNN"),
      History(0, "上昇志向", "JSSK"),
      History(0, "結婚してください", "KSK"),
      History(0, "これからやろうと思ってたのにな〜やる気無くなっちゃったな〜", "KYOYNN"),
      History(0, "上昇志向", "JSSK"),
      History(0, "結婚してください", "KSK"),
      History(0, "これからやろうと思ってたのにな〜やる気無くなっちゃったな〜", "KYOYNN"),
      History(0, "上昇志向", "JSSK"),
      History(0, "結婚してください", "KSK"),
      History(0, "これからやろうと思ってたのにな〜やる気無くなっちゃったな〜", "KYOYNN"),
    )) {}
  }
}