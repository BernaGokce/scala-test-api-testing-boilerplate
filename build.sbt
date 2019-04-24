name := "ScalaTraning"

version := "0.1"

scalaVersion := "2.12.8"

//Scala Test
libraryDependencies ++= Seq(
  //HTTP client
  "com.softwaremill.sttp" %% "core" % "1.5.12",

  // Json Parser
  "com.softwaremill.sttp" %% "circe" % "1.5.12",
  "io.circe" %% "circe-core" % "0.10.0",
  "io.circe" %% "circe-parser" % "0.10.0",

  // Doobie - DB library
  "org.tpolecat" %% "doobie-core"     % "0.6.0",
  "org.tpolecat" %% "doobie-postgres" % "0.6.0",

  // PostgreSQL
  "org.postgresql" % "postgresql" % "42.2.5",

  // Scala Test (GivenWhenThen)
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",

  //Reporting
  "org.pegdown" % "pegdown" % "1.6.0" % Test

)

testOptions in Test ++= Seq(Tests.Argument(TestFrameworks.ScalaTest, "-o"), Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports"))



