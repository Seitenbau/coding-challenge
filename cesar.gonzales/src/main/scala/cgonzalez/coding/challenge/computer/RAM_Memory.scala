package cgonzalez.coding.challenge.computer

import scala.collection.mutable


/**
 * Class for creating registers
 */
class Register() extends BinaryOperations {
  var binary = Array[Char]()

  def setBinary(value: Short) = {
    binary = getBinary(value)
    this

  }

  def getDecimal: Short = getDecimal(binary)
}

/**
 * Object that represents the Registers
 */
object Registers extends BinaryOperations {

  val registers = mutable.HashMap(
    'A' -> new Register().setBinary(0),
    'B' -> new Register().setBinary(0),
    'C' -> new Register().setBinary(0)
  )

  var Z: Boolean = false

  var IP: Int = 0

  def increaseIp() = {
    IP = IP + 1
  }

  var stop: Boolean = false

  override def toString(): String = {
    registers.toList.reverse.foldLeft("")((accu, x) => accu.concat(x._1.toString + "->" + getDecimal(x._2.binary) + "\n")) + "Z->" + Z + "\nIP->" + IP
  }
}


/**
 * Object that represents the Memory RAM
 * 256 bytes
 */
object RAM_Memory {
  var size: Int = _
  var memory: Array[Char] = _

  /**
   * Initialization of the RAM Memory in bytes
   * @param s Int
   */
  def init(s: Int) {
    size = s * 8
    memory = (0 to size - 1).map(x => '0').toArray
  }

  /**
   * Set data in the Memory
   * @param offset Int
   * @param value Array[Char]
   */
  def setMemory(offset: Int, value: Array[Char]) = {
    for (index <- 0 to value.size - 1) {
      memory(index) = value(index)
    }
  }

  /**
   * Read data from the Memory
   * @param offset Int
   * @param size  Int
   * @return Array[Char]
   */
  def readMemory(offset: Int, size: Int): Array[Char] = {
    (offset to offset + size - 1).map(index => memory(index)).toArray

  }
}
