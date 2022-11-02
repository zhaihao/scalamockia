scalaVersion := "2.13.10"
name         := "scalamockia"
organization := "me.ooon"

Global / excludeLintKeys := Set(idePackagePrefix)

idePackagePrefix := Some("me.ooon.scalamockia")

libraryDependencies ++= Seq(NSCALA, OS_LIB, SQUANTS, ORISON, TYPESAFE_CONFIG, PLAY_JSON, MOCK)
libraryDependencies ++= Seq(SCALA_TEST, LOG).flatten

excludeDependencies in Global ++= excludes
dependencyOverrides in Global ++= overrides
