package me.donnie.reader.hots

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.donnie.reader.data.ReaderClient
import me.donnie.reader.data.entities.Article
import me.donnie.reader.data.entities.Result

class HotsViewModel : ViewModel() {
  
  private val _result = MutableLiveData<Result<List<Article>>>()
  val result: LiveData<Result<List<Article>>> = _result
  
  fun getHotNews() = viewModelScope.launch {
    _result.value = ReaderClient.getHotNews()
  }
  
}