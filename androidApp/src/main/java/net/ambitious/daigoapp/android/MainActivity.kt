package net.ambitious.daigoapp.android

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.MobileAds
import net.ambitious.daigoapp.android.compose.*
import net.ambitious.daigoapp.android.ui.AppTheme

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {

  private lateinit var viewModel: MainViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    MobileAds.initialize(this)
    viewModel = ViewModelProvider(this)[MainViewModel::class.java]

    setContent {

      val words = viewModel.words.collectAsState()
      val input = viewModel.input.collectAsState()
      val result = viewModel.result.collectAsState()
      val loading = viewModel.loading.collectAsState()
      val createButtonEnable = viewModel.createButtonEnable.collectAsState()
      val proposal = viewModel.proposal.collectAsState()
      val rules = viewModel.rules.collectAsState()
      val isMenuShow = viewModel.isMenuShow.collectAsState()

      val scope = rememberCoroutineScope()

      AppTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colors.background
        ) {
          ErrorDialogCompose(viewModel.errorDialog.collectAsState().value) {
            viewModel.dismissErrorDialog()
          }

          RulesDialogCompose(rules.value) {
            viewModel.dismissRules()
          }

          LoadingCompose(loading.value)

          ModalBottomSheetLayout(
            sheetState = viewModel.resultBottomSheet,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetContent = {
              if (isMenuShow.value) {
                MenuCompose {
                  viewModel.showRules(it)
                }
              } else {
                ResultModalCompose(
                  viewModel.showProposal,
                  result.value,
                  input.value,
                  proposal.value,
                  { viewModel.setInputWord(it, true) },
                  { viewModel.proposalButtonClick() }
                )
              }
            }
          ) {
            AllViews(
              input.value,
              createButtonEnable.value,
              words.value,
              { viewModel.setInputWord(it, false) },
              { viewModel.buttonClick(scope) },
              { viewModel.showMenu(scope) }
            )
          }
        }
      }
    }
  }
}

@Composable
fun AllViews(
  input: String,
  createButtonEnable: Boolean,
  words: List<String>,
  onTextChange: (String) -> Unit = {},
  buttonClick: () -> Unit = {},
  showMenuClick: () -> Unit = {}
) {
  Box(Modifier.fillMaxSize()) {
    NativeAdCompose()
    Box(Modifier.align(Alignment.BottomCenter)) {
      Column {
        SamplesCompose(onTextChange, words)
        InputCompose(input, createButtonEnable, onTextChange, buttonClick, showMenuClick)
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
      createButtonEnable = true,
      words = sampleWords
    )
  }
}
