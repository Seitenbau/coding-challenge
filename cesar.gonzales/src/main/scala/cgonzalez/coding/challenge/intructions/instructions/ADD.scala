package cgonzalez.coding.challenge.intructions.instructions

import cgonzalez.coding.challenge.computer.{Registers, Register}
import cgonzalez.coding.challenge.intructions._

/**
 * class that represent the ADD instruction
 * reg1 <- reg2 + reg3 ; Z=0;
 * @param registerResult Char
 * @param register1  Char
 * @param register2  Char
 */
case class ADD(registerResult: Char, register1: Char, register2: Char) extends Instruction {
  def process {
    val reg1 = Registers.registers.get(register1).get.binary
    val reg2 = Registers.registers.get(register2).get.binary
    val result = new Register()
    result.binary = add(reg1, reg2)
    Registers.registers.update(registerResult, result)

  }
}
