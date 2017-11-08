package qiita_twitter_bot

import org.junit.Test
import scala.util.Random

final class Spec {

  @Test
  def `create Item objects`(): Unit = {
    Main.getEntries("Scala").foreach { item =>
      val str = item.tweetString(Set("Scala", "Scalajp"))
      println(str)

      assert(item.link startsWith "http")
      assert(Item.escape(item.description).contains("#") == false)
    }
  }

  @Test
  def `eval config.scala`(): Unit = {
    Eval.fromFileName[Config](Main.defaultConfigName)
  }

  @Test
  def `test resizeTweetString`(): Unit = {
    (0 to 2000).foreach { n =>
      val str = List
        .fill(n) {
          if (Random.nextBoolean()) {
            Random.nextPrintableChar()
          } else {
            Random.nextInt.toChar
          }
        }
        .mkString

      val result = Item.resizeTweetString(str)
      val max = 280
      assert(result.length <= max)
      val ascii = result.count(Item.isASCII)
      val nonAscii = result.length - ascii
      assert(ascii + (nonAscii * 2) <= max)
      assert(str.startsWith(result))
    }
  }
}
