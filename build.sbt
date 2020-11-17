// Copyright (C) 2011-2012 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

// http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

name := "ta2020scala"

version := "0.1"

scalaVersion := "2.12.6"

maintainer := "Anton Rechenauer <antonrechenauer@gmail.com>"

packageSummary := "TA2020 debian package"

packageDescription := """A sw for creating local files"""

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-unchecked",
  "-feature",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-Ywarn-dead-code",
  "-Xfatal-warnings"
)

libraryDependencies ++= Seq(

  "com.typesafe.slick" %% "slick" % "3.3.0",
  "org.slf4j" % "slf4j-nop" % "1.7.25",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.0",
  "org.xerial" % "sqlite-jdbc" % "3.27.2",
  "org.postgresql" % "postgresql" % "42.2.5",
  "org.apache.poi" % "poi" % "4.0.1",
  "org.apache.poi" % "poi-ooxml" % "4.0.1",
  "org.apache.commons" % "commons-math3" % "3.6.1",
)

mainClass in (Compile, run) := Some("Starter")

assemblyJarName in assembly := "ta2020.jar"

mainClass in assembly := Some("Starter")

scapegoatVersion in ThisBuild := "1.3.8"

scalaBinaryVersion in ThisBuild := "2.12"

//wartremoverWarnings ++= Warts.all

//enablePlugins(SbtTwirl)

enablePlugins(JavaAppPackaging)

enablePlugins(DockerPlugin)

//TwirlKeys.templateImports += "ta2020._"
