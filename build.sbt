lazy val commonSettings = Seq(
  organization := "com.andysprague",
  version := "1.0.0",
  scalaVersion := "2.11.2"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
      name := "impatient",
      libraryDependencies ++= Seq(
        "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
        "org.scalacheck" % "scalacheck_2.11" % "1.12.5" % "test"
      )
  )

