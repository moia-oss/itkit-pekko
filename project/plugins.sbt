// SBT plugin resolvers
resolvers +=
  Resolver.url("bintray-sbilinski", url("https://dl.bintray.com/sbilinski/maven"))(Resolver.ivyStylePatterns)

// Use git in sbt, show git prompt and use versions from git.
// sbt> git <your git command>
addSbtPlugin("com.github.sbt" % "sbt-git" % "2.1.0")

// Automatically adds license information to each source code file.
addSbtPlugin("de.heikoseeberger" % "sbt-header" % "5.10.0")

// Formatting in scala
// See .scalafmt.conf for configuration details.
// Formatting takes place before the project is compiled.
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.5")

// Static code analysis.
// sbt> scapegoat
addSbtPlugin("com.sksamuel.scapegoat" % "sbt-scapegoat" % "1.2.13")

// Publish to sonatype
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.12.2")

// publishSigned
addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.3.1")
