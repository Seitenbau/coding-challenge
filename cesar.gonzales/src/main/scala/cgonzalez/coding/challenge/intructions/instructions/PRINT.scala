package cgonzalez.coding.challenge.intructions.instructions

import cgonzalez.coding.challenge.intructions.Instruction
import cgonzalez.coding.challenge.computer.Registers

/**
 * class that represent the PRINT instruction
 * prints reg value IP+=1 ; Z=0; (no linebreak)
 * @param reg Char
 */
case class PRINT(reg: Char) extends Instruction {
  def process: Unit = {
    print(Registers.registers.get(reg).get.getDecimal)
  }
}
