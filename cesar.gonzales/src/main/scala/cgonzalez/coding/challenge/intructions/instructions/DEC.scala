package cgonzalez.coding.challenge.intructions.instructions

import cgonzalez.coding.challenge.computer.{Registers, Register}
import cgonzalez.coding.challenge.intructions.Instruction

/**
 * class that represent the DEC instruction
 * reg <- reg-1 ; IP+=1 ; Z=0;
 * @param register  Char
 */
case class DEC(register: Char) extends Instruction {
  def process: Unit = {
    val reg = Registers.registers.get(register).get.binary
    val result = new Register()
    result.binary = dec(reg)
    Registers.registers.update(register, result)
  }
}
