package cgonzalez.coding.challenge.intructions.instructions

import cgonzalez.coding.challenge.intructions.Instruction
import cgonzalez.coding.challenge.computer.Registers

/**
 * class that represent the STOP instruction
 * stop execution ; Z=0;
 */
class STOP extends Instruction {
  def process: Unit = {
    Registers.increaseIp
    println("\n\n" + Registers.toString())
    Registers.stop = true
  }
}
