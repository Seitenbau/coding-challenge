package cgonzalez.coding.challenge.intructions.instructions

import cgonzalez.coding.challenge.computer.{RAM_Memory, Registers, Register}
import cgonzalez.coding.challenge.intructions.Instruction

/**
 * class that represent the LOADM instruction
 * reg <- reg OR value ; IP+=1 ; Z=0;
 * @param registerName Char
 * @param value Long
 */
case class LOADM(registerName: Char, value: Long) extends Instruction {
  def process {
    val register = new Register
    register.binary = RAM_Memory.readMemory(value.toInt, 16)
    Registers.registers.put(registerName, register)
  }
}
