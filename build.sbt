name := """TubeLytics"""
organization := "com.tubelytics"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.15"

libraryDependencies += guice
libraryDependencies ++= Seq(
  guice,
  "com.google.apis" % "google-api-services-youtube" % "v3-rev222-1.25.0", ws
)
