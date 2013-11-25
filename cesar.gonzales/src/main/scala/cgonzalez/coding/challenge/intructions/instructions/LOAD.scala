package cgonzalez.coding.challenge.intructions.instructions

import cgonzalez.coding.challenge.computer.{Registers, Register}
import cgonzalez.coding.challenge.intructions.Instruction

/**
 * class that represent the LOAD instruction
 * reg <- value ; IP+=1 ; Z=0;
 * @param registerName Char
 * @param value Long
 */
case class LOAD(registerName: Char, value: Long) extends Instruction {
  def process {
    val register = new Register().setBinary(value.toShort)
    Registers.registers.put(registerName, register)
  }
}
