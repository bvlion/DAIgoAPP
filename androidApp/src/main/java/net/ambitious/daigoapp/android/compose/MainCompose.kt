package net.ambitious.daigoapp.android.compose

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import net.ambitious.daigoapp.android.R
import net.ambitious.daigoapp.android.ui.AppTheme

@Composable
@ExperimentalComposeUiApi
fun InputCompose(
  input: String,
  createButtonEnable: Boolean,
  onTextChange: (String) -> Unit = {},
  buttonClick: () -> Unit = {},
  showMenuClick: () -> Unit = {}
) {
  val keyboardController = LocalSoftwareKeyboardController.current
  val focusManager = LocalFocusManager.current
  Column {
    OutlinedTextField(
      value = input,
      onValueChange = onTextChange,
      label = { Text("略したい言葉") },
      modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth(),
      singleLine = true,
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
      keyboardActions = KeyboardActions(
        onDone = {
          keyboardController?.hide()
          focusManager.clearFocus()
        }
      )
    )

    Box(
      Modifier
        .padding(16.dp)
        .fillMaxWidth()
    ) {
      Button(onClick = buttonClick,
        Modifier
          .height(40.dp)
          .fillMaxWidth()
          .padding(horizontal = 56.dp),
        enabled = createButtonEnable
      ) {
        Text("生成")
      }
      IconButton(
        onClick = showMenuClick,
        modifier = Modifier.requiredSize(36.dp).align(Alignment.CenterEnd)
      ) {
        Icon(
          painterResource(R.drawable.ic_hamburger),
          contentDescription = "menu",
          Modifier.padding(end = 8.dp)
        )
      }
    }
  }
}

@Composable
fun SamplesCompose(
  onTextChange: (String) -> Unit,
  words: List<String>
) {
  FlowRow(
    modifier = Modifier.fillMaxWidth().padding(16.dp),
    mainAxisSpacing = 12.dp,
    crossAxisSpacing = 8.dp
  ) {
    words.forEach {
      Card(
        backgroundColor = MaterialTheme.colors.onSurface,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.clickable {
          onTextChange(it)
        }
      ) {
        Text(
          it,
          modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
          color = MaterialTheme.colors.surface,
        )
      }
    }
  }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
@ExperimentalComposeUiApi
fun MainPreview() {
  AppTheme {
//    SampleArea({}, sampleWords)
    InputCompose("努力大事", true)
  }
}

val sampleWords = arrayListOf(
  "努力大事",
  "DAIGO大誤算",
  "グイグイヨシコイ",
  "上昇志向",
  "負ける気がしない"
)
