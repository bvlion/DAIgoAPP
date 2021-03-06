package net.ambitious.daigoapp.android

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.ambitious.daigoapp.API
import net.ambitious.daigoapp.call.Result

@ExperimentalMaterialApi
class MainViewModel : ViewModel() {
  private val api = API()

  private val _words = MutableStateFlow(emptyList<String>())
  val words = _words.asStateFlow()

  private val _input = MutableStateFlow("")
  val input = _input.asStateFlow()

  private val _result = MutableStateFlow("")
  val result = _result.asStateFlow()

  private val _loading = MutableStateFlow(false)
  val loading = _loading.asStateFlow()

  private val _createButtonEnable = MutableStateFlow(false)
  val createButtonEnable = _createButtonEnable.asStateFlow()

  private val _proposal = MutableStateFlow("")
  val proposal = _proposal.asStateFlow()

  private val _errorDialog = MutableStateFlow<Result.ErrorDetail?>(null)
  val errorDialog = _errorDialog.asStateFlow()

  private val _rules = MutableStateFlow("")
  val rules = _rules.asStateFlow()

  private val _isMenuShow = MutableStateFlow(false)
  val isMenuShow = _isMenuShow.asStateFlow()

  val showProposal = mutableStateOf(false)
  val resultBottomSheet = ModalBottomSheetState(ModalBottomSheetValue.Hidden)

  fun setInputWord(input: String, isProposal: Boolean) {
    if (isProposal) {
      _proposal
    } else {
      _input
    }.value = input
    _createButtonEnable.value = input.isNotEmpty()
  }

  fun buttonClick(scope: CoroutineScope) {
    _loading.value = true
    api.getDaigo(input.value) {
      _loading.value = false
      when (it) {
        is Result.Success -> {
          it.data.text.let { res ->
            setInputWord(res, true)
            _result.value = res
          }
          _isMenuShow.value = false
          scope.launch {
            resultBottomSheet.show()
          }
        }
        is Result.Failure -> _errorDialog.value = it.err
      }
    }
  }

  fun proposalButtonClick() {
    _loading.value = true
    api.postDaigo(input.value, proposal.value) {
      _loading.value = false
      when (it) {
        is Result.Success -> {
          _result.value = proposal.value
          showProposal.value = false
        }
        is Result.Failure -> _errorDialog.value = it.err
      }
    }
  }

  fun dismissErrorDialog() {
    _errorDialog.value = null
  }

  fun showMenu(scope: CoroutineScope) {
    _isMenuShow.value = true
    scope.launch {
      resultBottomSheet.show()
    }
  }

  fun showRules(isPrivacyPolicy: Boolean) {
    api.getRules(isPrivacyPolicy) {
      when (it) {
        is Result.Success -> it.data.text.let { res ->
          _rules.value = res
        }
        is Result.Failure -> _errorDialog.value = it.err
      }
    }
  }

  fun dismissRules() {
    _rules.value = ""
  }

  init {
    api.getSamples {
      if (it is Result.Success) {
        _words.value = it.data.samples
      }
    }
  }

  override fun onCleared() {
    super.onCleared()
    api.cancel()
  }
}