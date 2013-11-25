package cgonzalez.coding.challenge.intructions.instructions

import cgonzalez.coding.challenge.computer.{Registers, Register}
import cgonzalez.coding.challenge.intructions.Instruction

/**
 * class that represent the SUB instruction
 * @param registerResult Char
 * @param register1 Char
 * @param register2 Char
 */
case class SUB(registerResult: Char, register1: Char, register2: Char) extends Instruction {
  def process {
    val reg1 = Registers.registers.get(register1).get
    val reg2 = Registers.registers.get(register2).get
    //    val as = reg1.binary ^ reg2.binary
    //    as

    val result = new Register().setBinary((reg1.getDecimal - reg2.getDecimal).toShort)
    Registers.registers.put(registerResult, result)

  }
}
