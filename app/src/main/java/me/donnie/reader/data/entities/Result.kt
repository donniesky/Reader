package me.donnie.reader.data.entities

sealed class Result<out T> {
  
  data class Success<out T>(val data: T, val responseModified: Boolean = true) : Result<T>()
  
  data class Failure(val code: Int = 0,
                     val desc: String = "failure") : Result<Nothing>()
  
  data class Error(val exception: Exception) : Result<Nothing>()
  
  override fun toString(): String {
    return when(this) {
      is Success<*> -> "Success[data=$data]"
      is Failure -> "Failure[code=$code, desc=$desc]"
      is Error -> "Error[exception=$exception]"
    }
  }
}