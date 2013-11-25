package cgonzalez.coding.challenge.intructions.instructions

import cgonzalez.coding.challenge.computer.Registers
import cgonzalez.coding.challenge.intructions.Instruction

/**
 * class that represent the JNE instruction
 * IP <- (Z == 0) ? IP + offset : IP + 1 ; Z=0;
 * @param offset Int
 */
case class JNE(offset: Int) extends Instruction {
  def process: Unit = {
    if (!Registers.Z)
      Registers.IP = Registers.IP - 1 + offset
  }
}




