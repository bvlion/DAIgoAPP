package net.ambitious.daigoapp.android.compose

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.ambitious.daigoapp.android.BuildConfig
import net.ambitious.daigoapp.android.R
import net.ambitious.daigoapp.android.ui.AppTheme

@Composable
fun ResultModalCompose(
  showProposal: MutableState<Boolean>,
  resultText: String,
  inputText: String,
  changeableResultText: String,
  onTextChange: (String) -> Unit = {},
  buttonClick: () -> Unit = {}
) {
  val context = LocalContext.current
  val appName = stringResource(R.string.app_name)

  ProposalDialogCompose(
    showProposal,
    inputText,
    changeableResultText,
    onTextChange,
    buttonClick
  )

  Column {
    Row(
      Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.Bottom
    ) {
      Text(
        inputText,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
      )

      Text(
        " は…",
        fontSize = 16.sp
      )
    }

    Text(
      resultText,
      modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
      textAlign = TextAlign.Center,
      fontSize = 40.sp,
      fontWeight = FontWeight.Bold
    )

    Row (
      Modifier
        .padding(16.dp)
        .align(Alignment.End)
    ) {
      TextButton({
        showProposal.value = true
      }) {
        Text("別の回答を提案")
      }

      TextButton({
        val sendIntent = Intent().apply {
          action = Intent.ACTION_SEND
          putExtra(Intent.EXTRA_TEXT, "$appName が「$inputText」を「$resultText」と変換しました！\nあなたも試してみよう☆\nhttps://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
          type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
      }) {
        Text("シェア")
      }
    }
  }
}

@Composable
fun ProposalDialogCompose(
  iShow: MutableState<Boolean>,
  inputText: String,
  resultText: String,
  onTextChange: (String) -> Unit = {},
  buttonClick: () -> Unit = {}
) {
  if (iShow.value) {
    val focusRequester = remember { FocusRequester() }
    var textFieldValue by remember {
      mutableStateOf(createTextFieldValue(resultText))
    }

    AlertDialog(
      onDismissRequest = {
        iShow.value = false
      },
      title = { Text("「$inputText」は正しくは…") },
      text = {
        Column {
          Text("", Modifier.height(8.dp)) // TODO use padding
          OutlinedTextField(
            value = textFieldValue,
            onValueChange = {
              if (it.text.matches("^[a-zA-Z]*\$".toRegex())) {
                textFieldValue = createTextFieldValue(it.text.uppercase())
                onTextChange(it.text.uppercase())
              }
                            },
            modifier = Modifier
              .fillMaxWidth()
              .focusRequester(focusRequester),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
              imeAction = ImeAction.Done,
              keyboardType = KeyboardType.Password
            )
          )
        }
      },
      dismissButton = {
        TextButton({ iShow.value = false }) { Text("閉じる") }
      },
      confirmButton = {
        TextButton({ buttonClick() }) { Text("送信") }
      },
      shape = RoundedCornerShape(8.dp),
    )
    LaunchedEffect(Unit) {
      focusRequester.requestFocus()
    }
  }
}

private fun createTextFieldValue(text: String) =
  TextFieldValue(
    text = text,
    selection = TextRange(text.length)
  )

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun ResultModalPreview() {
  AppTheme {
    ProposalDialogCompose(
      remember { mutableStateOf(true) },
      "グイグイヨシコイ",
      "GYK"
    )
  }
}
