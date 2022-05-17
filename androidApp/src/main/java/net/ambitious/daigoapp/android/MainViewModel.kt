package net.ambitious.daigoapp.android

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import net.ambitious.daigoapp.API
import net.ambitious.daigoapp.call.Result
import net.ambitious.daigoapp.domain.DaiGo

class MainViewModel : ViewModel() {
  private val api = API()

  private val _words = MutableStateFlow(emptyList<String>())
  val words = _words.asStateFlow()

  private val _input = MutableStateFlow("")
  val input = _input.asStateFlow()

  private val _result = MutableStateFlow(Result<DaiGo.GenerateResponse>())
  val result = _result.asStateFlow()

  private val _loading = MutableStateFlow(false)
  val loading = _loading.asStateFlow()

  private val _createButtonEnable = MutableStateFlow(false)
  val createButtonEnable = _createButtonEnable.asStateFlow()

  fun setInputWord(input: String) {
    _input.value = input
    _createButtonEnable.value = input.isNotEmpty()
  }

  fun buttonClick() {
    _loading.value = true
    api.getDaigo(input.value) {
      _loading.value = false
      _result.value = it
    }
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