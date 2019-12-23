package me.donnie.reader.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.donnie.reader.data.entities.Item
import me.donnie.reader.data.ReaderClient
import me.donnie.reader.data.entities.Result

class HomeViewModel : ViewModel() {
  
  private val _text = MutableLiveData<String>().apply {
    value = "This is home Fragment"
  }
  val text: LiveData<String> = _text
  
  private val _result = MutableLiveData<Result<List<Item>>>()
  val result: LiveData<Result<List<Item>>> = _result
  
  fun getNews() = viewModelScope.launch {
    _result.value = ReaderClient.getNews()
  }
}