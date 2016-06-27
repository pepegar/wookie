import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

organization := "io.github.pepegar"

name := "wookie"

version := "0.1-SNAPSHOT"

val sv = "2.11.8"

val dependencies = Seq(
  "com.amazonaws" % "aws-java-sdk" % "1.8.9.1" % "compile",
  "org.typelevel" %% "cats" % "0.6.0",
  "org.specs2" %% "specs2-core" % "3.8.4" % "test"
)

val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-encoding", "UTF-8", // 2 args
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-language:experimental.macros",
    "-unchecked",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-value-discard"
  )
)

scalacOptions in Test ++= Seq("-Yrangepos")

SbtScalariform.scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(RewriteArrowSymbols, true)
  .setPreference(AlignParameters, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(MultilineScaladocCommentsStartOnFirstLine, true)
  .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)

lazy val core = project
  .in(file("wookie-core"))
  .settings(commonSettings)
  .settings(scalaVersion := sv)
  .settings(libraryDependencies := dependencies)
  .settings(libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http-core" % "2.4.7",
    "com.typesafe.akka" %% "akka-http-experimental" % "2.4.7"))

lazy val dynamodb = project
  .in(file("wookie-dynamodb"))
  .settings(commonSettings)
  .settings(scalaVersion := sv)
  .settings(libraryDependencies := dependencies)
  .dependsOn(core)

lazy val s3 = project
  .in(file("wookie-s3"))
  .settings(commonSettings)
  .settings(scalaVersion := sv)
  .settings(libraryDependencies := dependencies)
  .dependsOn(core)
