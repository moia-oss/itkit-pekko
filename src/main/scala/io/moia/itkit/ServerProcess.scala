/*
 * Copyright (c) MOIA GmbH 2017
 */

package io.moia.itkit

import scala.sys.process.Process

/** Enhances a `Process` with port information. */
trait ServerProcess extends Process {
  val port: Int
}
