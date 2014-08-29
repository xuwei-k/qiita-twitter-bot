package qiita_twitter_bot

import scala.concurrent.duration._

abstract class Config{
  val twitter: TwitterSettings
  def tag: String
  val hashtags: Set[String] = Set(tag)
  val tweetInterval: Duration = 1.second
  val interval: Duration
  val dbSize: Int = 100
  val firstTweet: Boolean = false
  val blockUsers: Set[String] = Set.empty
}

abstract class TwitterSettings{
  val consumerKey: String
  val consumerSecret: String
  val accessToken: String
  val accessTokenSecret: String
}

