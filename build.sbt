name := "ta2020scala"

version := "0.1"

scalaVersion := "2.12.6"


libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "com.typesafe" % "config" % "1.3.2",
  "com.typesafe.slick" %% "slick" % "3.2.3",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.3",
  "org.xerial" % "sqlite-jdbc" % "3.23.1"
)

mainClass in (Compile, run) := Some("Starter")

assemblyJarName in assembly := "ta2020.jar"

mainClass in assembly := Some("Starter")