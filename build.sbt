name := "qiita-twitter-bot"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.3"

organization := "com.github.xuwei-k"

licenses += ("MIT License", url("http://opensource.org/licenses/MIT"))

homepage := Some(url("https://github.com/xuwei-k/qiita-twitter-bot"))

resolvers ++= Seq(
 Opts.resolver.sonatypeReleases
 ,"twitter4j" at "http://twitter4j.org/maven2"
)

libraryDependencies ++= Seq(
  "org.twitter4j" % "twitter4j-core" % "3.0.3"
 ,"com.twitter" %% "util-eval" % "6.5.0"
 ,"org.json4s" %% "json4s-native" % "3.2.5"
 ,"org.specs2"  %% "specs2" % "2.2.2" % "test"
)

scalacOptions ++= Seq("-deprecation", "-Xlint", "-language:_")

assemblySettings

AssemblyKeys.jarName in AssemblyKeys.assembly <<= (name, version).map{ (name, version) =>
  val df = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
  <x>{name}-{df.format(new java.util.Date)}-{version}.jar</x>.text
}

watchSources += file("config.scala")

sourcesInBase := false

