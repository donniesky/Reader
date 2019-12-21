package me.donnie.reader.data

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "rss")
data class Rss(
  @Element
  val channel: Channel
)

@Root(name = "channel")
data class Channel(
  @Element
  val title: String,
  @Element
  val link: String,
  @Element
  val description: String,
  @ElementList(inline = true)
  val item: List<Item>?
)

@Root(name = "item")
data class Item(
  @Element
  val title: String,
  @Element
  val link: String,
  @Element
  val description: String,
  @Element
  val category: String,
  @Element
  val pubDate: String,
  @Element(name = "thumbnail")
  val image: Image
)

@Root(name = "thumbnail", strict = false)
data class Image(
  @Attribute
  val url: String,
  @Attribute
  val width: String,
  @Attribute
  val height: String
)