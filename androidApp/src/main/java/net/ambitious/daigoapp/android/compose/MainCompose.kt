package net.ambitious.daigoapp.android.compose

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import net.ambitious.daigoapp.API
import net.ambitious.daigoapp.android.R
import net.ambitious.daigoapp.android.ui.AppTheme
import net.ambitious.daigoapp.call.Result

@Composable
fun InputArea(
  input: MutableState<String>,
  result: MutableState<String>,
  loading: MutableState<Boolean>,
  createButtonEnable: MutableState<Boolean>,
  api: API,
  errorDialog: MutableState<Result.ErrorDetail?> = remember { mutableStateOf(null) }
) {
  ErrorDialogCompose(errorDialog)

  Column {
    Text(
      result.value,
      modifier = Modifier
        .padding(all = 16.dp)
        .fillMaxWidth(),
      textAlign = TextAlign.Center,
      fontSize = 32.sp,
      fontWeight = FontWeight.Bold
    )

    OutlinedTextField(
      value = input.value,
      onValueChange = {
        input.value = it
        createButtonEnable.value = it.isNotEmpty()
      },
      label = { Text("D◯I 語") },
      modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth()
    )

    Box(
      Modifier
        .padding(16.dp)
        .fillMaxWidth()
    ) {
      Button(
        {
          loading.value = true
          api.getDaigo(input.value) {
            loading.value = false
            when (it) {
              is Result.Success -> result.value = it.data.text
              is Result.Failure -> errorDialog.value = it.err
            }
          }
        },
        Modifier
          .height(40.dp)
          .fillMaxWidth()
          .padding(horizontal = 56.dp),
        enabled = createButtonEnable.value
      ) {
        Text("生成")
      }
      Icon(
        painterResource(R.drawable.ic_hamburger),
        contentDescription = "menu",
        Modifier
          .requiredSize(36.dp)
          .padding(end = 8.dp)
          .align(Alignment.CenterEnd)
      )
    }
  }
}

@Composable
fun SampleArea(
  input: MutableState<String>,
  createButtonEnable: MutableState<Boolean>,
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
          input.value = it
          createButtonEnable.value = true
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
fun DefaultPreview() {
  val input = remember { mutableStateOf("努力大事") }
  val result = remember { mutableStateOf("DD") }
  val loading = remember { mutableStateOf(true) }
  val createButtonEnable = remember { mutableStateOf(false) }

  AppTheme {
    SampleArea(input, createButtonEnable, sampleWords)
//    InputArea(input, result, loading, createButtonEnable, API())
  }
}

val sampleWords = arrayListOf(
  "努力大事",
  "DAIGO大誤算",
  "グイグイヨシコイ",
  "上昇志向",
  "負ける気がしない"
)
