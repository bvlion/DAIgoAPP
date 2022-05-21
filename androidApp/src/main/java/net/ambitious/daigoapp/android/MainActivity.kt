package net.ambitious.daigoapp.android

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import net.ambitious.daigoapp.android.compose.InputArea
import net.ambitious.daigoapp.android.compose.LoadingCompose
import net.ambitious.daigoapp.android.compose.SampleArea
import net.ambitious.daigoapp.android.compose.sampleWords
import net.ambitious.daigoapp.android.ui.AppTheme
import net.ambitious.daigoapp.call.Result
import net.ambitious.daigoapp.domain.DaiGo

class MainActivity : ComponentActivity() {

  private lateinit var viewModel: MainViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProvider(this)[MainViewModel::class.java]

    setContent {

      val words = viewModel.words.collectAsState()
      val input = viewModel.input.collectAsState()
      val result = viewModel.result.collectAsState()
      val loading = viewModel.loading.collectAsState()
      val createButtonEnable = viewModel.createButtonEnable.collectAsState()

      AppTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colors.background
        ) {
          AllViews(
            input.value,
            result.value,
            loading.value,
            createButtonEnable.value,
            words.value,
            { viewModel.setInputWord(it) },
            { viewModel.buttonClick() }
          )
        }
      }
    }
  }
}

@Composable
fun AllViews(
  input: String,
  result: Result<DaiGo.GenerateResponse>,
  loading: Boolean,
  createButtonEnable: Boolean,
  words: List<String>,
  onTextChange: (String) -> Unit = {},
  buttonClick: () -> Unit = {}
) {
  Box(Modifier.fillMaxSize()) {
    LoadingCompose(loading)
    Box(Modifier.align(Alignment.BottomCenter)) {
      Column {
        SampleArea(onTextChange, words)
        InputArea(input, result, createButtonEnable, onTextChange, buttonClick)
      }
    }
  }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultPreview() {
  AppTheme {
    AllViews(
      input = "努力大事",
      result = Result.success(DaiGo.GenerateResponse("DD")),
      loading = false,
      createButtonEnable = true,
      words = sampleWords
    )
  }
}
