package qiita_twitter_bot

import scala.util.control.Exception.allCatch
import com.twitter.util.Eval
import java.io.File
import java.net.URL
import org.json4s._
import org.json4s.native.JsonMethods

object Main{
  val qiita = "https://qiita.com/api/v1/tags/" + (_: String) + "/items"

  val defaultConfigName = "config.scala"

  def main(args: Array[String]){
    val file = new File(
      allCatch.opt(args.head).getOrElse(defaultConfigName)
    )
    run(file)
  }

  def run(file: File){
    val conf = Eval[Config](file)
    import conf._

    val db = new DB[ITEM_URL](dbSize)
    val client = TweetClient(twitter)

    def tweet(data: Seq[Item]){
      data.reverseIterator.foreach{ entry =>
        Thread.sleep(tweetInterval.inMillis)
        client.tweet(entry.tweetString(hashtags))
      }
    }

    def entries() = {
      val c = Eval[Config](file)
      getEntries(c.tag, c.blockUsers)
    }

    val firstData = entries()
    db.insert(firstData.map{_.link}:_*)
    println("first insert data = " + firstData)
    if(firstTweet){
      tweet(firstData)
    }

    @annotation.tailrec
    def _run(){
      Thread.sleep(interval.inMillis)
      allCatchPrintStackTrace{
        val oldIds = db.selectAll
        val newData = entries().filterNot{a => oldIds.contains(a.link)}
        db.insert(newData.map{_.link}:_*)
        tweet(newData)
      }
      _run()
    }

    _run()
  }

  def getEntries(tag: String, blockUsers: Set[String] = Set.empty): Seq[Item] = {
    val in = StreamInput(new URL(qiita(tag)).openStream)
    JsonMethods.parse(in).children.map{
      Item.apply
    }.filterNot{ e =>
      val a = blockUsers.contains(e.user)
      if(a)println("block ! " + e)
      a
    }
  }
}
