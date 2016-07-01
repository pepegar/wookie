import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

val dependencies = Seq(
  "com.amazonaws" % "aws-java-sdk" % "1.8.9.1" % "compile",
  "org.typelevel" %% "cats" % "0.6.0",
  "org.specs2" %% "specs2-core" % "3.8.4" % "test"
)

val commonSettings = SbtScalariform.scalariformSettings ++ Seq(
  organization := "io.github.pepegar",
  version := "0.1-SNAPSHOT",
  scalaVersion := "2.11.8",
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
  ),
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(RewriteArrowSymbols, true)
  .setPreference(AlignParameters, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(MultilineScaladocCommentsStartOnFirstLine, true)
  .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)
)

scalacOptions in Test ++= Seq("-Yrangepos")

lazy val core = project
  .in(file("wookie-core"))
  .settings(commonSettings)
  .settings(libraryDependencies ++= dependencies)

lazy val dynamodb = project
  .in(file("wookie-dynamodb"))
  .settings(commonSettings)
  .settings(libraryDependencies ++= dependencies)
  .dependsOn(core)

lazy val s3 = project
  .in(file("wookie-s3"))
  .settings(commonSettings)
  .settings(libraryDependencies ++= dependencies)
  .dependsOn(core)

lazy val akkahttp = project
  .in(file("client/wookie-akka-http"))
  .settings(commonSettings)
  .settings(libraryDependencies ++= dependencies)
  .settings(libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http-core" % "2.4.7",
    "com.typesafe.akka" %% "akka-http-experimental" % "2.4.7"))
  .dependsOn(core)

lazy val docsSettings = commonSettings ++ ghpages.settings ++ tutSettings ++ Seq(
    git.remoteRepo := "git@github.com:pepegar/wookie.git",
    tutSourceDirectory := sourceDirectory.value / "tut",
    tutTargetDirectory := sourceDirectory.value / "pamphlet",
    sourceDirectory in Pamflet := sourceDirectory.value / "pamphlet",
    aggregate in doc := true
  )

lazy val docs = (project in file("docs"))
  .settings(moduleName := "wookie-docs")
  //.dependsOn(core)
  .enablePlugins(PamfletPlugin)
  .settings(docsSettings: _*)
  .settings(Seq(
    publish := (),
    publishLocal := (),
    publishArtifact := false))
  .dependsOn(core, dynamodb, s3)
