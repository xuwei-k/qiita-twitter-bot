name := "qiita-twitter-bot"

version := "0.1-SNAPSHOT"

scalaVersion := "2.12.13"

organization := "com.github.xuwei-k"

licenses += ("MIT License", url("http://opensource.org/licenses/MIT"))

homepage := Some(url("https://github.com/xuwei-k/qiita-twitter-bot"))

val twitter4jVersion = "4.0.7"

libraryDependencies ++= (
  ("org.scala-lang" % "scala-compiler" % scalaVersion.value) ::
    ("org.twitter4j" % "twitter4j-core" % twitter4jVersion) ::
    ("org.json4s" %% "json4s-native" % "4.0.0") ::
    ("com.novocode" % "junit-interface" % "0.11" % "test") ::
    Nil
)

val unusedWarnings = "-Ywarn-unused" :: Nil

scalacOptions ++= (
  "-deprecation" ::
    "-Xlint" ::
    "-language:postfixOps" ::
    "-language:existentials" ::
    "-language:higherKinds" ::
    "-language:implicitConversions" ::
    Nil
) ::: unusedWarnings

Seq(Compile, Test).flatMap(c => scalacOptions in (c, console) ~= { _.filterNot(unusedWarnings.toSet) })

assemblyJarName in assembly := {
  val df = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm")
  s"""${name.value}-${df.format(new java.util.Date)}-twitter4j-${twitter4jVersion}.jar"""
}

watchSources += file("config.scala")

sourcesInBase := false

resourceGenerators in Compile += task(
  Seq(baseDirectory.value / "build.sbt")
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")
