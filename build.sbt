name := "TripleTriadOnline"

version := "1.0"

lazy val `root` = (project in file(".")).enablePlugins(PlayJava, SbtWeb, PlayEbean)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  javaJdbc,
  filters,
  javaWs,
  guice,
  "org.postgresql" % "postgresql" % "9.4.1212.jre7",
  "org.mindrot" % "jbcrypt" % "0.3m"
)

includeFilter in(Assets, LessKeys.less) := "*.less"
LessKeys.compress in Assets := true

pipelineStages := Seq(concat)
pipelineStages in Assets := Seq(concat)

