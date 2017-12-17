logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += Resolver.sonatypeRepo("releases")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.7")
addSbtPlugin("com.typesafe.sbt" % "sbt-play-ebean" % "4.0.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.1.2")
addSbtPlugin("net.ground5hark.sbt" % "sbt-concat" % "0.1.9")