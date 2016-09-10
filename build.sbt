name := "dsc-codec"

version := "1.0"

crossPaths := false

autoScalaLibrary := false

fork in run := true

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.21",
  "ch.qos.logback" % "logback-core" % "1.1.7",
  "ch.qos.logback" % "logback-classic" % "1.1.7"
)
