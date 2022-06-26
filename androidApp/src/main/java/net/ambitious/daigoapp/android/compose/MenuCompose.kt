package net.ambitious.daigoapp.android.compose

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import net.ambitious.daigoapp.android.BuildConfig
import net.ambitious.daigoapp.android.ui.AppTheme

@Composable
fun MenuCompose(show: (Boolean) -> Unit) {
  val context = LocalContext.current
  Column(Modifier.fillMaxWidth().padding(16.dp)) {
    TextButton(onClick = { show(false) }) {
      Text("利用規約")
    }
    TextButton(onClick = { show(true) }) {
      Text("プライバシーポリシー")
    }
    TextButton(onClick = {
      context.startActivity(Intent(
        Intent.ACTION_VIEW,
        Uri.parse(try {
          "market://details?id=${BuildConfig.APPLICATION_ID}"
        } catch (_: ActivityNotFoundException) {
          "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        })
      ))
    }) {
      Text("レビューする")
    }
    TextButton(onClick = {
      context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
        "https://docs.google.com/forms/d/e/1FAIpQLSecLnJGQu3C-24UMTcji_jnt7kRqgtTjQglKyA8xu6ILdnrDQ/viewform"
      )))
    }) {
      Text("ご意見")
    }
  }
}

@ExperimentalComposeUiApi
@Composable
fun RulesDialogCompose(
  rule: String,
  dismissClick: () -> Unit = {}
) {
  if (rule.isNotEmpty()) {
    val textColor = String.format("#%06X", 0xFFFFFF and MaterialTheme.colors.onBackground.toArgb())
    val backgroundColor = String.format("#%06X", 0xFFFFFF and MaterialTheme.colors.background.toArgb())
    AlertDialog(
      onDismissRequest = dismissClick,
      text = {
        AndroidView(
          modifier = Modifier.fillMaxWidth().fillMaxHeight(),
          factory = ::WebView,
          update = {
            it.loadData(
              String.format(HTML_BODY, textColor, backgroundColor, rule),
              "text/html",
              "utf-8"
            )
          }
        )
      },
      properties = DialogProperties(usePlatformDefaultWidth = false),
      confirmButton = {
        TextButton(dismissClick) { Text("閉じる") }
      },
      shape = RoundedCornerShape(8.dp),
      modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(16.dp)
    )
  }
}

const val HTML_BODY = """
<!DOCTYPE HTML>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/github-markdown-css/4.0.0/github-markdown.min.css" rel="stylesheet" type="text/css" media="all" />
  <style>.small { font-size: 70%% !important; color: %s; }</style>
</head>
<body style="background-color: %s;">
  <div class="container">
    <div class="markdown-body small">
      %s
    </div>
  </div>
</body>
</html>
"""

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@ExperimentalComposeUiApi
@Composable
fun MenuPreview() {
  AppTheme {
    RulesDialogCompose("<h1>土台</h1>利用規約です")
  }
}