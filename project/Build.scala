import com.typesafe.sbt.SbtStartScript
import sbt.Keys._
import sbt._
import sbtassembly.Plugin.AssemblyKeys._
import sbtassembly.Plugin._
import spray.revolver.RevolverPlugin._

object BuildSettings {
  val buildOrganization = "com.acme"
  val buildVersion = "0.1"
  val buildScalaVersion = "2.11.8"

  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := buildOrganization,
    version := buildVersion,
    scalaVersion := buildScalaVersion,
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions", "-language:postfixOps", "-language:reflectiveCalls", "-encoding", "utf8"),
    javaOptions += "-Xmx1G",
    shellPrompt := ShellPrompt.buildShellPrompt
  )
}

object Resolvers {
  val typesafe = "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases"
  val spray = "Spray repo" at "http://repo.spray.io/"
  val akka = "Akka Releases" at "http://repo.akka.io/releases/"
  val sonatypeReleases = "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"
  val repositories = Seq(typesafe, spray, akka, sonatypeReleases)
}

object Dependencies {
  val sprayVersion = "1.3.1-20140423"
  val akkaVersion = "2.3.2"

  val spray = Seq(
    "io.spray" %% "spray-can" % sprayVersion,
    "io.spray" %% "spray-routing" % sprayVersion,
    "io.spray" %% "spray-json" % "1.2.6",
    "io.spray" %% "spray-client" % sprayVersion,
    "io.spray" %% "spray-testkit" % sprayVersion % "test"
  )

  val akka = Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
  )

  val logging = Seq(
    "ch.qos.logback" % "logback-classic" % "1.1.7",
    "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2"
  )

  val testing = Seq(
    "org.specs2" %% "specs2" % "2.5" % "test"
  )

  val dependencies = spray ++ akka ++ logging ++ testing
}

object ThisBuild extends Build {

  import BuildSettings._
  import Dependencies._
  import Resolvers._

  val name = "api"
  lazy val api = Project(
    name, file("."),
    settings = buildSettings
      ++ Seq(resolvers := repositories, libraryDependencies ++= dependencies)
      ++ assemblySettings
  )
}

