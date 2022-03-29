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

// ####################
// graphql
// ####################
libraryDependencies ++= Seq(
  "org.sangria-graphql" %% "sangria" % "2.1.6",
  "org.sangria-graphql" %% "sangria-play-json" % "2.0.2"
)

// ####################
// akka
// ####################
val AkkaVersion = "2.6.18"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-kafka" % "3.0.0",
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-discovery" % AkkaVersion
)

// ####################
// scalikejdbc
// ####################
libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % "3.5.0",
  "org.scalikejdbc" %% "scalikejdbc-config" % "3.5.0",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.8.0-scalikejdbc-3.5",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

// ####################
// mysql
// ####################
libraryDependencies += "mysql" % "mysql-connector-java" % "6.0.6"

// ####################
// play.server
// ####################
PlayKeys.devSettings := Seq("play.server.http.port" -> "9000")
// websocketの接続を維持するため開発環境のみidleTimeoutをinfiniteに設定
PlayKeys.devSettings += "play.server.http.idleTimeout" -> "infinite"
