package cgonzalez.coding.challenge

import cgonzalez.coding.challenge.computer.{RAM_Memory, Registers, Processor}
import scala.collection.JavaConversions._

/**
 * Object to execute the Application
 */
object Application extends App with Configuration {
  RAM_Memory.init(memorySize)
  while (!Processor(instructionsList(Registers.IP)).execute) {
  }

}