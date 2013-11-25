package cgonzalez.coding.challenge.intructions

import cgonzalez.coding.challenge.computer.{Registers, BinaryOperations}

/**
 * A template trait for instructions.
 *
 */
trait Instruction extends BinaryOperations {

  /**
   * method that implements the logic of the instruction
   */
  protected def process

  /**
   * Execute the
   * @return return true if there is not a stop instruction
   */
  def execute: Boolean = {
    process
    Registers.increaseIp
    Registers.stop
  }

}






