name := "ta2020scala"

version := "0.1"

scalaVersion := "2.12.6"


libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "com.typesafe" % "config" % "1.3.2" )

mainClass in (Compile, run) := Some("Starter")

assemblyJarName in assembly := "ta2020.jar"

mainClass in assembly := Some("Hallo")