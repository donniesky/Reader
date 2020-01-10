package me.donnie.reader.data.entities

import com.google.gson.annotations.SerializedName

data class Word(
  val id: String,
  val word: String,
  @SerializedName("pron")
  val phoneticUK: String,
  @SerializedName("pronaudio")
  val ukAudio: String,
  @SerializedName("pron_mei")
  val phoneticUS: String,
  @SerializedName("pronaudio_mei")
  val usAudio: String,
  val related: List<Affix>?,
  @SerializedName("count_click")
  val heat: String,
  @SerializedName("def")
  val definitions: List<Definition>?,
  @SerializedName("sent")
  val usage: List<Usage>?
)

data class Definition(
  val id: String,
  val word: String,
  @SerializedName("pos")
  val type: String,
  @SerializedName("trans")
  val definition: String
)

data class Usage(
  val id: String,
  val word: String,
  @SerializedName("orig")
  val example: String,
  @SerializedName("trans")
  val translation: String
)

data class Affix(
  val id: String,
  val word: String,
  val wid: String,
  @SerializedName("cigen")
  val root: Root?
)

data class Root(
  val id: String,
  val name: String,
  val type: String,
  @SerializedName("trans")
  val definitions: List<Content>?
)

data class Content(
  val id: String,
  val cid: String,
  val content: String
)