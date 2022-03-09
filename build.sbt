name := """scala-graphql-chat"""
organization := "com.aktumitaha.scala-graphql-chat"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.8"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.aktumitaha.scala-graphql-chat.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.aktumitaha.scala-graphql-chat.binders._"

libraryDependencies ++= Seq(
  "org.sangria-graphql" %% "sangria" % "2.1.6",
  "org.sangria-graphql" %% "sangria-play-json" % "2.0.2"
)

PlayKeys.devSettings := Seq("play.server.http.port" -> "9000")
