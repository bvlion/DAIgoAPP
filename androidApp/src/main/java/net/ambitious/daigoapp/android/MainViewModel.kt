package net.ambitious.daigoapp.android

import android.app.Application
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.ambitious.daigoapp.API
import net.ambitious.daigoapp.android.data.AppDataStore
import net.ambitious.daigoapp.android.data.AppDatabase
import net.ambitious.daigoapp.android.data.History
import net.ambitious.daigoapp.call.Result

@ExperimentalMaterialApi
class MainViewModel(application: Application) : AndroidViewModel(application) {
  private val api = API()
  private val dataStore = AppDataStore.getDataStore(getApplication<Application>().applicationContext)
  private val db = Room.databaseBuilder(
    getApplication<Application>().applicationContext,
    AppDatabase::class.java,
    "abbreviation-db"
  ).build()

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

  private val _viewMode = MutableStateFlow(AppDataStore.ViewMode.DEFAULT)
  val viewMode = _viewMode.asStateFlow()

  private val _histories = MutableStateFlow<List<History>?>(null)
  val histories = _histories.asStateFlow()

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
            db.historyDao().insert(History(text = input.value, abbreviation = res))
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
    _rules.value = "${api.rulesUrl}$isPrivacyPolicy"
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
    viewModelScope.launch {
      dataStore.getViewMode.collect {
        _viewMode.value = it
      }
    }
  }

  fun setViewMode(viewMode: AppDataStore.ViewMode) {
    _viewMode.value = viewMode
    viewModelScope.launch {
      dataStore.setViewMode(viewMode)
    }
  }

  fun setHistoryShow(isShow: Boolean) {
    if (isShow) {
      viewModelScope.launch(Dispatchers.IO) {
        _histories.value = db.historyDao().getAll()
      }
    } else {
      _histories.value = null
    }
  }

  override fun onCleared() {
    super.onCleared()
    api.cancel()
  }
}