package me.donnie.reader.data.entities

import com.google.gson.annotations.SerializedName

data class HotsNews(
  @SerializedName("errorcode")
  val code: String,
  val msg: String,
  val count: String,
  val data: Hots?
)

data class Hots(
  val article: List<Article>?
)

data class Article(
  val id: String,
  val title: String,
  val url: String,
  val image: String,
  @SerializedName("createtime")
  val date: String
)