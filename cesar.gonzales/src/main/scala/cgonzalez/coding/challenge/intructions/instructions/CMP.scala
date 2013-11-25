package cgonzalez.coding.challenge.intructions.instructions

import cgonzalez.coding.challenge.computer.Registers
import cgonzalez.coding.challenge.intructions.Instruction

/**
 * class that represent CMP instruction
 * Z <- (reg == value) ? 1 : 0 ; IP+=1;
 * @param reg Char
 * @param value Long
 */
case class CMP(reg: Char, value: Long) extends Instruction {
  def process: Unit = {
    Registers.Z = Registers.registers.get(reg).get.getDecimal == value
  }
}
