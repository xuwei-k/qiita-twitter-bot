package qiita_twitter_bot

import org.specs2.Specification
import com.twitter.util.Eval 
import java.io.File

final class Spec extends Specification{ def is=

  "create Item object" ! {
    {item: Item =>
      val str = item.tweetString(Set("Scala","Scalajp"))
      println(str)

      {
        item.link should be startWith "http"
      }and{
        str.size must be_<= (Item.LIMIT)
      }and{
        str must not contain("@")
      }and{
        Item.escape(item.description) must not contain("#")
      }
    }.forall(Main.getEntries("Scala"))
  } ^ "eval config.scala" ! {
    Eval[Config](new File(Main.defaultConfigName))
    success
  } ^ end

}


