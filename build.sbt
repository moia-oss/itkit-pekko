// *****************************************************************************
// Projects
// *****************************************************************************

lazy val itkit =
  project
    .in(file("."))
    .settings(
      name         := "itkit-pekko",
      organization := "io.moia",
      licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),
      scmInfo  := Some(ScmInfo(url("https://github.com/moia-oss/itkit-pekko"), "scm:git@github.com:moia-oss/itkit-pekko.git")),
      homepage := Some(url("https://github.com/moia-oss/itkit-pekko"))
    )
    .enablePlugins(
      AutomateHeaderPlugin,
      GitVersioning,
      GitBranchPrompt
    )
    .settings(sonatypeSettings: _*)
    .settings(commonSettings)
    .settings(libraryDependencies ++= library.dependencies)

lazy val samples =
  project
    .in(file("samples"))
    .configs(IntegrationWithTest)
    .dependsOn(itkit)
    .settings(IntegrationTestSettings: _*)
    .settings(commonSettings)
    .settings(
      fork            := true,
      publishArtifact := false
    )

lazy val IntegrationWithTest     = config("it").extend(Test)
lazy val IntegrationTestSettings = inConfig(IntegrationWithTest)(IntegrationTestConfig)
lazy val IntegrationTestConfig =
  Defaults.configSettings ++ Defaults.testTasks ++ org.scalafmt.sbt.ScalafmtPlugin.scalafmtConfigSettings(IntegrationWithTest) ++ Seq(
    IntegrationWithTest / publish / skip    := true,
    IntegrationWithTest / fork              := true,
    IntegrationWithTest / scalaSource       := baseDirectory.value / "src" / "it" / "scala",
    IntegrationWithTest / resourceDirectory := baseDirectory.value / "src" / "it" / "resources"
  )

// *****************************************************************************
// Dependencies
// *****************************************************************************

lazy val library = new {
  object Version {
    val pekko        = "1.2.0"
    val pekkoHttp    = "1.2.0"
    val log4j        = "2.25.2"
    val pureConfig   = "0.17.9"
    val scalaCheck   = "1.19.0"
    val scalaLogging = "3.9.6"
    val scalaTest    = "3.2.19"
  }

  val dependencies = Seq(
    // compile time dependencies
    "org.apache.pekko"           %% "pekko-actor-typed"         % Version.pekko,
    "org.apache.pekko"           %% "pekko-http"                % Version.pekkoHttp,
    "org.apache.pekko"           %% "pekko-http-testkit"        % Version.pekkoHttp,
    "org.apache.pekko"           %% "pekko-stream-typed"        % Version.pekko,
    "org.apache.pekko"           %% "pekko-actor-testkit-typed" % Version.pekko,
    "org.apache.logging.log4j"    % "log4j-api"                 % Version.log4j,
    "org.apache.logging.log4j"    % "log4j-core"                % Version.log4j,
    "org.apache.logging.log4j"    % "log4j-jul"                 % Version.log4j,
    "org.apache.logging.log4j"    % "log4j-slf4j2-impl"         % Version.log4j,
    "com.github.pureconfig"      %% "pureconfig-core"           % Version.pureConfig,
    "com.github.pureconfig"      %% "pureconfig-generic-base"   % Version.pureConfig,
    "org.scalacheck"             %% "scalacheck"                % Version.scalaCheck,
    "com.typesafe.scala-logging" %% "scala-logging"             % Version.scalaLogging,
    "org.scalatest"              %% "scalatest"                 % Version.scalaTest
  )
}

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val commonSettings =
  compilerSettings ++
    gitSettings ++
    licenseSettings ++
    sbtSettings ++
    scalaFmtSettings ++
    sbtGitSettings

lazy val compilerSettings = Seq(
  crossScalaVersions                                                 := Seq("2.13.16", "3.7.3"),
  versionScheme                                                      := Some("early-semver"),
  Compile / packageBin / mappings += baseDirectory.value / "LICENSE" -> "LICENSE",
  scalacOptions ++= Seq(
    "-unchecked",
    "-deprecation",
    "-release",
    "8",
    "-encoding",
    "UTF-8",
    "-DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector",
    "-Xlint:_,-byname-implicit",
    "-Ywarn-numeric-widen",
    "-Ywarn-dead-code",
    "-Ywarn-value-discard",
    "-Xsource:3"
  ),
  javacOptions ++= Seq(
    "-source",
    "1.8",
    "-target",
    "1.8"
  ),
  Compile / unmanagedSourceDirectories := Seq((Compile / scalaSource).value),
  Test / unmanagedSourceDirectories    := Seq((Test / scalaSource).value)
)

lazy val gitSettings = Seq(git.useGitDescribe := true)

lazy val licenseSettings = Seq(
  headerLicense  := Some(HeaderLicense.Custom("Copyright (c) MOIA GmbH 2017")),
  headerMappings := headerMappings.value + (HeaderFileType.conf -> HeaderCommentStyle.hashLineComment)
)

lazy val sonatypeSettings = {
  import xerial.sbt.Sonatype._
  Seq(
    publishTo              := sonatypePublishTo.value,
    sonatypeProfileName    := organization.value,
    publishMavenStyle      := true,
    sonatypeProjectHosting := Some(GitHubHosting("moia-oss", "itkit-pekko", "oss-support@moia.io")),
    credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credential")
  )
}

lazy val sbtSettings = Seq(cancelable in Global := true)

lazy val scalaFmtSettings = Seq(scalafmtOnCompile := true)

lazy val sbtVersionRegex = "v([0-9]+.[0-9]+.[0-9]+)-?(.*)?".r

lazy val sbtGitSettings = Seq(
  git.useGitDescribe       := true,
  git.baseVersion          := "0.0.0",
  git.uncommittedSignifier := None,
  git.gitTagToVersionNumber := {
    case sbtVersionRegex(v, "")         => Some(v)
    case sbtVersionRegex(v, "SNAPSHOT") => Some(s"$v-SNAPSHOT")
    case sbtVersionRegex(v, s)          => Some(s"$v-$s-SNAPSHOT")
    case _                              => None
  }
)
