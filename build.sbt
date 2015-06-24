name := "qiita-twitter-bot"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

organization := "com.github.xuwei-k"

licenses += ("MIT License", url("http://opensource.org/licenses/MIT"))

homepage := Some(url("https://github.com/xuwei-k/qiita-twitter-bot"))

resolvers ++= Seq(
 Opts.resolver.sonatypeReleases
 ,"twitter4j" at "http://twitter4j.org/maven2"
)

val twitter4jVersion = "4.0.4"

libraryDependencies ++= (
  ("org.scala-lang" % "scala-compiler" % scalaVersion.value) ::
  ("org.twitter4j" % "twitter4j-core" % twitter4jVersion) ::
  ("org.json4s" %% "json4s-native" % "3.2.10") ::
  ("com.novocode" % "junit-interface" % "0.11" % "test") ::
  Nil
)

scalacOptions ++= (
  "-deprecation" ::
  "-Xlint" ::
  "-language:postfixOps" ::
  "-language:existentials" ::
  "-language:higherKinds" ::
  "-language:implicitConversions" ::
  "-Ywarn-unused" ::
  "-Ywarn-unused-import" ::
  Nil
)

assemblySettings

AssemblyKeys.jarName in AssemblyKeys.assembly <<= name.map{ name =>
  val df = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm")
  s"""${name}-${df.format(new java.util.Date)}-twitter4j-${twitter4jVersion}.jar"""
}

watchSources += file("config.scala")

sourcesInBase := false

resourceGenerators in Compile += task(
  Seq(baseDirectory.value / "build.sbt")
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")
