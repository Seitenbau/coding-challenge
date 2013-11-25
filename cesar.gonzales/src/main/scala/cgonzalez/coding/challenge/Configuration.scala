package cgonzalez.coding.challenge

/**
 * A template trait for Configuration the Application.
 *
 */

import com.typesafe.config.ConfigFactory

object Configuration {
  private val config = ConfigFactory.load

}

trait Configuration {

  def config = Configuration.config

  def memorySize = config.getInt("memory_size")

  def instructionsList = config.getStringList("instructions")

}