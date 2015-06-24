package qiita_twitter_bot

import org.junit.Test

final class Spec {

  @Test
  def `create Item objects`(): Unit = {
    Main.getEntries("Scala").foreach{ item =>
      val str = item.tweetString(Set("Scala","Scalajp"))
      println(str)

      assert(item.link startsWith "http")
      assert(str.size <= Item.LIMIT)
      assert(Item.escape(item.description).contains("#") == false)
    }
  }

  @Test
  def `eval config.scala`(): Unit = {
    Eval.fromFileName[Config](Main.defaultConfigName)
  }

}
