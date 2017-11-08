package qiita_twitter_bot

import twitter4j._
import twitter4j.conf._

case class TweetClient(conf: TwitterSettings) {

  val t = {
    val c = new ConfigurationBuilder
    c.setDebugEnabled(true)
      .setOAuthConsumerKey(conf.consumerKey)
      .setOAuthConsumerSecret(conf.consumerSecret)
      .setOAuthAccessToken(conf.accessToken)
      .setOAuthAccessTokenSecret(conf.accessTokenSecret)

    new TwitterFactory(c.build()).getInstance()
  }

  def tweet(str: String, retryCharCount: Option[Int]): Unit = {
    allCatchPrintStackTrace {
      try {
        t.updateStatus(str)
      } catch {
        case e: Throwable =>
          retryCharCount match {
            case Some(count) =>
              Main.printDateTime()
              println(e)
              t.updateStatus(str.take(count))
            case _ =>
              Main.printDateTime()
              e.printStackTrace()
          }
      }
    }
  }

}
