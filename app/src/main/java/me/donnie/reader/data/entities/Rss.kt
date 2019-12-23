package me.donnie.reader.data.entities

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
data class Rss(
  @field:Element(name = "channel")
  @param:Element(name = "channel")
  val channel: Channel
)

@Root(name = "channel", strict = false)
data class Channel(
  @field:Element(name = "title")
  @param:Element(name = "title")
  val title: String,
  @field:Element(name = "link")
  @param:Element(name = "link")
  val link: String,
  @field:Element(name = "language", required = false)
  @param:Element(name = "language", required = false)
  val language: String,
  @field:Element(name = "description")
  @param:Element(name = "description")
  val description: String,
  @field:ElementList(name = "item", inline = true)
  @param:ElementList(name = "item", inline = true)
  val item: List<Item>?
)

@Root(name = "item", strict = false)
data class Item(
  @field:Element(name = "title")
  @param:Element(name = "title")
  val title: String,
  @field:Element(name = "link")
  @param:Element(name = "link")
  val link: String,
  @field:Element(name = "description")
  @param:Element(name = "description")
  val description: String,
  @field:Element(name = "category")
  @param:Element(name = "category")
  val category: String,
  @field:Element(name = "pubDate")
  @param:Element(name = "pubDate")
  val pubDate: String,
  @field:Element(name = "guid", required = false)
  @param:Element(name = "guid", required = false)
  val guid: String,
  @field:Element(name = "thumbnail", required = false)
  @param:Element(name = "thumbnail", required = false)
  val image: Image? = null
)

@Root(name = "thumbnail", strict = false)
data class Image(
  @field:Attribute(name = "url")
  @param:Attribute(name = "url")
  val url: String,
  @field:Attribute(name = "width")
  @param:Attribute(name = "width")
  val width: String,
  @field:Attribute(name = "height")
  @param:Attribute(name = "height")
  val height: String
)