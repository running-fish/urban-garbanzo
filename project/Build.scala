import sbt._
import sbt.Keys._
import sbtassembly.AssemblyPlugin.autoImport._
import sbtprotobuf.ProtobufPlugin._
import sbtunidoc.Plugin.unidocSettings
import spray.revolver.RevolverPlugin.Revolver


object CueinioBuild extends Build {
  object Ver {
    val finagle = "6.30.0"
    val finatra = "2.1.2"
    val aws = "1.10.32"
    val guice = "4.0"
  }

  object Lib {
    // Finagle
    val finagleMysql = "com.twitter" %% "finagle-mysql" % Ver.finagle

    // Finatra
    val finatraHttp = "com.twitter.finatra" %% "finatra-http" % Ver.finatra
    val finatraHttpclient = "com.twitter.finatra" %% "finatra-httpclient" % Ver.finatra
    val finatraJackson = "com.twitter.finatra" %% "finatra-jackson" % Ver.finatra
    val finatraUtils = "com.twitter.finatra" %% "finatra-utils" % Ver.finatra
    val finatraSl4j = "com.twitter.finatra" %% "finatra-slf4j" % Ver.finatra
    val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.1.3"
    val injectCore = "com.twitter.inject" %% "inject-core" % Ver.finatra
    val injectApp = "com.twitter.inject" %% "inject-app" % Ver.finatra

    // Test
    val mockitoCore = "org.mockito" % "mockito-core" % "1.9.5" % "test"
    val scalatest = "org.scalatest" %% "scalatest" % "2.2.3" % "test"
    val spec2 = "org.specs2" %% "specs2" % "2.3.12" % "test"
    val finatraHttpTest = "com.twitter.finatra" %% "finatra-http" % Ver.finatra % "test"
    val finatraJacksonTest = "com.twitter.finatra" %% "finatra-jackson" % Ver.finatra % "test"
    val injectServerTest = "com.twitter.inject" %% "inject-server" % Ver.finatra % "test"
    val injectAppTest = "com.twitter.inject" %% "inject-app" % Ver.finatra % "test"
    val injectCoreTest = "com.twitter.inject" %% "inject-core" % Ver.finatra % "test"
    val injectModulesTest = "com.twitter.inject" %% "inject-modules" % Ver.finatra % "test"
    val injectExtensionsTest = "com.google.inject.extensions" % "guice-testlib" % Ver.guice % "test"

    val finatraHttpTestTests = "com.twitter.finatra" %% "finatra-http" % Ver.finatra % "test" classifier "tests"
    val finatraJacksonTestTests = "com.twitter.finatra" %% "finatra-jackson" % Ver.finatra % "test" classifier "tests"
    val injectServerTestTests = "com.twitter.inject" %% "inject-server" % Ver.finatra % "test" classifier "tests"
    val injectAppTestTests = "com.twitter.inject" %% "inject-app" % Ver.finatra % "test" classifier "tests"
    val injectCoreTestTests = "com.twitter.inject" %% "inject-core" % Ver.finatra % "test" classifier "tests"
    val injectModulesTestTests = "com.twitter.inject" %% "inject-modules" % Ver.finatra % "test" classifier "tests"
    val injectExtensionsTestTests = "com.google.inject.extensions" % "guice-testlib" % Ver.guice % "test" classifier "tests"
  }

  val defaultSettings = Revolver.settings ++ Seq(
    version := "1.0.0",
    organization := "io.cuein",
    scalaVersion := "2.11.7",
    scalacOptions := Seq(
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Xlint",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused",
      "-Ywarn-unused-import"
    ),
    fork in run := true,
    resolvers ++= Seq(
      "twitter" at "http://maven.twttr.com/",
      "typesafe" at "http://repo.typesafe.com/typesafe/releases/"
    ),
    libraryDependencies ++= Seq(
      Lib.logbackClassic,
      Lib.mockitoCore,
      Lib.scalatest,
      Lib.spec2,
      Lib.injectAppTest,
      Lib.injectCoreTest,
      Lib.injectModulesTest,
      Lib.injectExtensionsTest,
      Lib.injectAppTestTests,
      Lib.injectCoreTestTests,
      Lib.injectModulesTestTests,
      Lib.injectExtensionsTestTests
    )
  )

  val assemblySettings = sbtassembly.AssemblyPlugin.assemblySettings ++ Seq(
    assemblyMergeStrategy in assembly <<= (assemblyMergeStrategy in assembly) { (wrapped) =>
      {
        case "BUILD" => MergeStrategy.discard
        case other => wrapped(other)
      }
    }
  )

  val databaseSettings = Seq(
    libraryDependencies ++= Seq(
      Lib.finagleMysql
    )
  )

  val httpServiceSettings = Seq(
    libraryDependencies ++= Seq(
      Lib.finatraHttp,
      Lib.finatraHttpclient,
      Lib.finatraJackson,
      Lib.finatraSl4j,

      Lib.finatraHttpTest,
      Lib.finatraJacksonTest,
      Lib.injectServerTest,
      Lib.finatraHttpTestTests,
      Lib.finatraJacksonTestTests,
      Lib.injectServerTestTests
    )
  )

  lazy val root = Project(
    id = "root",
    base = file("."),
    aggregate = Seq(
      proto,
      www
    ),
    settings = unidocSettings
  ).disablePlugins(sbtassembly.AssemblyPlugin)

  lazy val proto = Project(
    id = "proto",
    base = file("proto"),
    settings = defaultSettings ++ protobufSettings ++
      inConfig(protobufConfig)(Seq[Setting[_]](version := "2.6.1"))
  ).disablePlugins(sbtassembly.AssemblyPlugin)

  lazy val www = Project(
    id = "www",
    base = file("www"),
    settings = assemblySettings ++ defaultSettings ++ databaseSettings ++ httpServiceSettings
  ).dependsOn(proto)
}
