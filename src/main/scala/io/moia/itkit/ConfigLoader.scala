/*
 * Copyright (c) MOIA GmbH 2017
 */

package io.moia
package itkit

import pureconfig.ConfigSource
import pureconfig.ConfigReader

object ConfigLoader extends Logger {
  def load(): Config =
    ConfigSource.default.at("itkit").load[Config] match {
      case Right(config) =>
        config

      case Left(failures) =>
        log.error("Could not read application config due to: {}.\nLoading default configuration.", failures)
        Config()
    }

  implicit private val clientPekkaConfigReader: ConfigReader[ClientPekkaConfig] =
    ConfigReader.forProduct1("loglevel")(ClientPekkaConfig.apply)
  implicit private val clientConfigReader: ConfigReader[ClientConfig]   = ConfigReader.forProduct1("pekko")(ClientConfig.apply)
  implicit private val processConfigReader: ConfigReader[ProcessConfig] = ConfigReader.forProduct10(
    "await-log-timeout",
    "initial-memory-allocation-pool",
    "maximum-memory-allocation-pool",
    "conc-g-c-threads",
    "parallel-g-c-threads",
    "parallelism-min",
    "factor",
    "parallelism-max",
    "min-threads",
    "max-threads"
  )(ProcessConfig.apply)
  implicit private val configReader: ConfigReader[Config] = ConfigReader.forProduct2("process", "client")(Config.apply)
}
