package qiita_twitter_bot

import scala.concurrent.duration._

abstract class Config {
  val twitter: TwitterSettings
  def tag: String
  val hashtags: Set[String] = Set(tag)
  val tweetInterval: Duration = 1.second
  val interval: Duration
  val dbSize: Int = 100
  val firstTweetCount: Int
  val blockUsers: Set[String] = Set.empty
  val retryCharCount: Option[Int] = Some(140)
  def resizeTweetString(tweet: String): String =
    Item.resizeTweetString(tweet)
}

abstract class TwitterSettings {
  val consumerKey: String
  val consumerSecret: String
  val accessToken: String
  val accessTokenSecret: String
}
