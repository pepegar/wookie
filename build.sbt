organization := "aws"

name := "default"

version := "0.1-SNAPSHOT"

val sv = "2.11.8"

val dependencies = Seq(
  "com.amazonaws" % "aws-java-sdk" % "1.8.9.1" % "compile",
  "org.typelevel" %% "cats" % "0.6.0",
  "com.typesafe.akka" %% "akka-http-core" % "2.4.7",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.7"
)

lazy val core = project
  .in(file("aws-core"))
  .settings(scalaVersion := sv)
  .settings(libraryDependencies := dependencies)

lazy val dynamodb = project
  .in(file("aws-dynamodb"))
  .settings(scalaVersion := sv)
  .settings(libraryDependencies := dependencies)
  .dependsOn(core)
