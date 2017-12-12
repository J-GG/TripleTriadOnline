name := "TripleTriadOnline"

version := "1.0"

lazy val `untitled` = (project in file(".")).enablePlugins(PlayJava, SbtWeb)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(javaJdbc, filters, javaWs, guice)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

includeFilter in(Assets, LessKeys.less) := "*.less"
LessKeys.compress in Assets := true

pipelineStages := Seq(concat)
pipelineStages in Assets := Seq(concat)

