package qiita_twitter_bot

import org.json4s.JValue
import org.json4s.DefaultReaders._

final case class Item private (
  link: ITEM_URL,
  title: String,
  description: String,
  user: String
) {
  import Item._

  def tweetString(hashtags: Set[String] = Set.empty): String = {
    val tags = hashtags.collect { case s if !s.isEmpty => "#" + s }.mkString(" ")
    Iterator(
      link,
      escape(title),
      tags,
      escape(description)
    ).mkString("\n").take(LIMIT)
  }
}

object Item {
  final val LIMIT = 140

  private[this] val escapeMap = Map(
    "@" -> "",
    "#" -> "♯"
  )

  def escape(str: String): String =
    escapeMap.foldLeft(str) { case (s, (k, v)) => s.replace(k, v) }

  def apply(x: JValue): Item = Item(
    (x \ "url").as[String],
    (x \ "title").as[String],
    (x \ "body").as[String],
    (x \ "user" \ "id").as[String]
  )
}
