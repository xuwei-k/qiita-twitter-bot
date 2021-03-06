package qiita_twitter_bot

import scala.util.control.Exception.allCatch
import java.io.File
import java.net.URL
import org.json4s._
import org.json4s.native.JsonMethods

object Main {
  val qiita = "https://qiita.com/api/v2/tags/" + (_: String) + "/items"

  val defaultConfigName = "config.scala"

  def main(args: Array[String]): Unit = {
    val file = new File(
      allCatch.opt(args.head).getOrElse(defaultConfigName)
    )
    run(file)
  }

  def run(file: File): Unit = {
    val env = Env.fromConfigFile(file)
    import env._
    import env.config._

    val firstData = getEntries(tag, blockUsers)
    db.insert(firstData.map { _.link }.toList)
    printDateTime()
    println("first insert data = " + firstData)
    firstData.take(firstTweetCount).reverseIterator.foreach { entry =>
      Thread.sleep(tweetInterval.toMillis)
      client.tweet(config.resizeTweetString(entry.tweetString(hashtags)), retryCharCount)
    }
    loop(env)
  }

  @annotation.tailrec
  def loop(env: Env): Unit = {
    import env._
    import env.config._
    try {
      Thread.sleep(interval.toMillis)
      val oldIds = db.selectAll
      val newData = getEntries(tag, blockUsers).filterNot { a =>
        oldIds.contains(a.link)
      }
      db.insert(newData.map { _.link }.toList)
      newData.reverseIterator.foreach { e =>
        Thread.sleep(env.config.tweetInterval.toMillis)
        env.client.tweet(config.resizeTweetString(e.tweetString(hashtags)), retryCharCount)
      }
    } catch {
      case e: Throwable =>
        printDateTime()
        e.printStackTrace()
    }
    loop(env.reload)
  }

  def getEntries(tag: String, blockUsers: Set[String] = Set.empty): Seq[Item] = {
    val json = scala.io.Source.fromURL(new URL(qiita(tag)), "UTF-8").mkString
    JsonMethods
      .parse(json)
      .children
      .map {
        Item.apply
      }
      .filterNot { e =>
        blockUsers.contains(e.user)
      }
  }

  def printDateTime(): Unit = {
    val df = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm")
    println(df.format(new java.util.Date))
  }
}
