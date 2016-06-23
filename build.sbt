organization := "io.github.pepegar"

name := "wookie"

version := "0.1-SNAPSHOT"

val sv = "2.11.8"

val dependencies = Seq(
  "com.amazonaws" % "aws-java-sdk" % "1.8.9.1" % "compile",
  "org.typelevel" %% "cats" % "0.6.0",
  "org.specs2" %% "specs2-core" % "3.8.4" % "test"
)

scalacOptions in Test ++= Seq("-Yrangepos")

lazy val core = project
  .in(file("wookie-core"))
  .settings(scalaVersion := sv)
  .settings(libraryDependencies := dependencies)
  .settings(libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http-core" % "2.4.7",
    "com.typesafe.akka" %% "akka-http-experimental" % "2.4.7"))

lazy val dynamodb = project
  .in(file("wookie-dynamodb"))
  .settings(scalaVersion := sv)
  .settings(libraryDependencies := dependencies)
  .dependsOn(core)

lazy val s3 = project
  .in(file("wookie-s3"))
  .settings(scalaVersion := sv)
  .settings(libraryDependencies := dependencies)
  .dependsOn(core)
