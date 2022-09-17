package net.ambitious.daigoapp.android.compose

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import net.ambitious.daigoapp.android.BuildConfig
import net.ambitious.daigoapp.android.data.ProjectDataStore
import net.ambitious.daigoapp.android.ui.AppTheme
import net.ambitious.daigoapp.android.ui.noRippleClickable
import java.net.URLEncoder

@Composable
fun MenuCompose(
  viewMode: ProjectDataStore.ViewMode,
  saveViewMode: (ProjectDataStore.ViewMode) -> Unit,
  show: (Boolean) -> Unit
) {
  val context = LocalContext.current
  Column(
    Modifier
      .fillMaxWidth()
      .padding(16.dp)) {
    TextButton(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.Start),
      onClick = { show(false) }
    ) {
      Text(modifier = Modifier.fillMaxWidth(), text = "利用規約")
    }
    TextButton(
      modifier = Modifier.fillMaxWidth(),
      onClick = { show(true) }
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
    Column(Modifier.padding(top = 8.dp)) {
      Text(
        fontSize = 14.sp,
        text = "Color"
      )
      Row {
        ProjectDataStore.ViewMode.values().forEach { mode ->
          RadioButton(
            selected = (mode == viewMode),
            onClick = { saveViewMode(mode) }
          )
          Text(
            modifier = Modifier.padding(top = 14.dp).noRippleClickable { saveViewMode(mode) },
            fontSize = 15.sp,
            style = MaterialTheme.typography.body1.merge(),
            text = when (mode) {
              ProjectDataStore.ViewMode.DEFAULT -> "システム"
              ProjectDataStore.ViewMode.LIGHT -> "ライト"
              ProjectDataStore.ViewMode.DARK -> "ダーク"
            }
          )
        }
      }
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

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@ExperimentalComposeUiApi
@Composable
fun MenuPreview() {
  AppTheme {
    MenuCompose(ProjectDataStore.ViewMode.DEFAULT, {}) {}
  }
}