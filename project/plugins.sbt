resolvers ++= Seq(
  Resolver.typesafeRepo("releases"),
  "jgit-repo" at "http://download.eclipse.org/jgit/maven")

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.6.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.0.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.5.4")
addSbtPlugin("org.tpolecat" % "tut-plugin" % "0.4.2")
