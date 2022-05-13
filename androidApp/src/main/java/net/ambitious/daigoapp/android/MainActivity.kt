package net.ambitious.daigoapp.android

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import net.ambitious.daigoapp.API
import net.ambitious.daigoapp.android.compose.InputArea
import net.ambitious.daigoapp.android.compose.LoadingCompose
import net.ambitious.daigoapp.android.compose.SampleArea
import net.ambitious.daigoapp.android.compose.sampleWords
import net.ambitious.daigoapp.android.ui.AppTheme
import net.ambitious.daigoapp.call.Result

class MainActivity : ComponentActivity() {

  private val words = mutableStateListOf<String>()
  private val api = API()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      AppTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colors.background
        ) {

          val input = remember { mutableStateOf("") }
          val result = remember { mutableStateOf("") }
          val loading = remember { mutableStateOf(false) }
          val createButtonEnable = remember { mutableStateOf(false) }

          AllViews(input, result, loading, createButtonEnable, words, api)
        }
      }
    }
  }

  override fun onResume() {
    super.onResume()
    api.getSamples {
      if (it is Result.Success) {
        words.addAll(it.data.samples)
      }
    }
  }

  override fun onDestroy() {
    api.cancel()
    super.onDestroy()
  }
}

@Composable
fun AllViews(
  input: MutableState<String>,
  result: MutableState<String>,
  loading: MutableState<Boolean>,
  createButtonEnable: MutableState<Boolean>,
  words: List<String>,
  api: API = API()
) {
  Box(Modifier.fillMaxSize()) {
    LoadingCompose(loading.value)
    Box(Modifier.align(Alignment.BottomCenter)) {
      Column {
        SampleArea(input, createButtonEnable, words)
        InputArea(input, result, loading, createButtonEnable, api)
      }
    }
  }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultPreview() {
  val input = remember { mutableStateOf("努力大事") }
  val result = remember { mutableStateOf("DD") }
  val loading = remember { mutableStateOf(true) }
  val createButtonEnable = remember { mutableStateOf(true) }
  AppTheme {
    AllViews(input, result, loading, createButtonEnable, sampleWords)
  }
}
