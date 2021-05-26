package qiita_twitter_bot

import org.json4s.JValue
import org.json4s.ReaderSyntax
import org.json4s.DefaultReaders._
import org.json4s.MonadicJValue._

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
    ).mkString("\n")
  }
}

object Item {
  private[this] val escapeMap = Map(
    "@" -> "",
    "#" -> "♯"
  )

  def escape(str: String): String =
    escapeMap.foldLeft(str) { case (s, (k, v)) => s.replace(k, v) }

  private[this] implicit def asReaderSyntax(j: JValue): ReaderSyntax =
    new ReaderSyntax(j)

  def apply(x: JValue): Item = Item(
    (x \ "url").as[String],
    (x \ "title").as[String],
    (x \ "body").as[String],
    (x \ "user" \ "id").as[String]
  )

  def isASCII(c: Char): Boolean = {
    (0x0 <= c) && (c <= 0x7f)
  }

  def resizeTweetString(tweet: String): String = {
    if (tweet.length <= 140) {
      tweet
    } else {
      val original = tweet.toCharArray
      val buf = new java.lang.StringBuilder()
      @annotation.tailrec
      def loop(i: Int, size: Int): Unit = {
        original.lift.apply(i) match {
          case Some(char) =>
            val nextSize = size + {
              if (isASCII(original(i))) {
                1
              } else {
                2
              }
            }
            if (nextSize > 280) {
              ()
            } else {
              buf.append(char)
              loop(i + 1, nextSize)
            }
          case None =>
            ()
        }
      }
      loop(0, 0)
      buf.toString
    }
  }
}
