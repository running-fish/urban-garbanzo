resolvers ++= Seq(
  "Twitter" at "https://maven.twttr.com/"
)

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.0")

addSbtPlugin("com.github.gseitz" % "sbt-protobuf" % "0.4.0")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.6.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.2.0")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.1")

addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.3.3")
