package net.ambitious.daigoapp.android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.ambitious.daigoapp.API
import net.ambitious.daigoapp.call.Result
import net.ambitious.daigoapp.domain.Samples

class MainViewModel : ViewModel() {
  private val api = API()

  private val words: LiveData<Samples>
    get() = _words
  private val _words = MutableLiveData<Samples>()

  init {
    api.getSamples {
      if (it is Result.Success) {
        _words.postValue(it.data)
      }
    }
  }

  override fun onCleared() {
    super.onCleared()
    api.cancel()
  }
}