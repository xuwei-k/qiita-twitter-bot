import qiita_twitter_bot._

import scala.concurrent.duration._

new Config {
  val tag = "scala"
  val interval = 1.hours
  val firstTweetCount = 1

  val twitter = new TwitterSettings {
    val consumerKey = ""
    val consumerSecret = ""
    val accessToken = ""
    val accessTokenSecret = ""
  }
}
