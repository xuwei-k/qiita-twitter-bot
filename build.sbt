name := "qiita-twitter-bot"

version := "0.1-SNAPSHOT"

scalaVersion := "2.12.15"

organization := "com.github.xuwei-k"

licenses += ("MIT License", url("http://opensource.org/licenses/MIT"))

homepage := Some(url("https://github.com/xuwei-k/qiita-twitter-bot"))

val twitter4jVersion = "4.0.7"

libraryDependencies ++= (
  ("org.scala-lang" % "scala-compiler" % scalaVersion.value) ::
    ("org.twitter4j" % "twitter4j-core" % twitter4jVersion) ::
    ("org.json4s" %% "json4s-native-core" % "4.0.5") ::
    ("com.github.sbt" % "junit-interface" % "0.13.3" % "test") ::
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

Seq(Compile, Test).flatMap(c => c / console / scalacOptions ~= { _.filterNot(unusedWarnings.toSet) })

(assembly / assemblyJarName) := {
  val df = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm")
  s"""${name.value}-${df.format(new java.util.Date)}-twitter4j-${twitter4jVersion}.jar"""
}

watchSources += file("config.scala")

sourcesInBase := false

(Compile / resourceGenerators) += task(
  Seq(baseDirectory.value / "build.sbt")
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")
