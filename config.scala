import qiita_twitter_bot._
import com.twitter.conversions.time._

new Config{
  val tag = "scala"
  val interval = 1.hours

  val twitter = new TwitterSettings{
    val consumerKey       = ""
    val consumerSecret    = ""
    val accessToken       = ""
    val accessTokenSecret = ""
  }
}

