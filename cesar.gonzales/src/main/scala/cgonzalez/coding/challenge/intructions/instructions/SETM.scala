package cgonzalez.coding.challenge.intructions.instructions

import cgonzalez.coding.challenge.computer.{RAM_Memory, Registers}
import cgonzalez.coding.challenge.intructions.Instruction

/**
 * class that represent the SETM instruction
 * ram[address] <- reg ; IP+=1 ; Z=0;
 * @param value Int
 * @param registerName Char
 */
case class SETM(value: Int, registerName: Char) extends Instruction {
  def process {
    RAM_Memory.setMemory(value, Registers.registers.get(registerName).get.binary)
  }
}
