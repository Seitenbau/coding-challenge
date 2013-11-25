package cgonzalez.coding.challenge.computer

/**
 * A template trait for binary Operations.
 *
 */
trait BinaryOperations {

  /**
   * Transform an Array of Bit in a Short
   * @param binary Array[Char]
   * @return  Short
   */
  def getDecimal(binary: Array[Char]): Short = {
    val s = if (binary(0) == '1') ("0", "1", -1) else ("1", "0", 1)
    val positive = binary.reverse.foldLeft("")((x, y) => {
      val z = y match {
        case '1' => s._1
        case '0' => s._2
      }
      z + x
    })
    (Integer.parseInt(positive, 2) * s._3).toShort
  }

  /**
   * Transform an Long into an binary Array[Char] of 16 bits
   * @param value Long
   * @return Array[Char]
   */
  def getBinary(value: Long): Array[Char] = {
    if (value > -1)
      Integer.toBinaryString(0x10000 | value.toInt).substring(1).toArray
    else {
      value.toBinaryString.toList.reverse.slice(0, 15).reverse.toArray
    }
  }

  /**
   * add 2 binary Array[Char] of 16 bits
   * @param a Array[Char]
   * @param b Array[Char]
   * @return  Array[Char]
   */
  def add(a: Array[Char], b: Array[Char]): Array[Char] = {
    getBinary((getDecimal(a) + getDecimal(b)).toShort)
  }

  /**
   * sub 2 binary Array[Char] of 16 bits
   * @param a Array[Char]
   * @param b Array[Char]
   * @return  Array[Char]
   */
  def sub(a: Array[Char], b: Array[Char]): Array[Char] = {
    getBinary((getDecimal(a) - getDecimal(b)).toShort)
  }

  /**
   * xor 2 binary Array[Char] of 16 bits
   * @param a Array[Char]
   * @param b Array[Char]
   * @return  Array[Char]
   */
  def xor(a: Array[Char], b: Array[Char]): Array[Char] = {
    getBinary((getDecimal(a) ^ getDecimal(b)).toShort)
  }

  /**
   * or 2 binary Array[Char] of 16 bits
   * @param a Array[Char]
   * @param b Array[Char]
   * @return  Array[Char]
   */
  def or(a: Array[Char], b: Array[Char]): Array[Char] = getBinary((getDecimal(a) | getDecimal(b)).toShort)

  /**
   * or 2 binary Array[Char] of 16 bits
   * @param a Array[Char]
   * @param b Array[Char]
   * @return  Array[Char]
   */
  def and(a: Array[Char], b: Array[Char]): Array[Char] = getBinary((getDecimal(a) & getDecimal(b)).toShort)

  /**
   * increase a binary Array[Char] of 16 bits by 1
   * @param a Array[Char]
   * @return  Array[Char]
   */
  def inc(a: Array[Char]): Array[Char] = getBinary((getDecimal(a) + 1).toShort)

  /**
   * decrease a binary Array[Char] of 16 bits by 1
   * @param a Array[Char]
   * @return  Array[Char]
   */
  def dec(a: Array[Char]): Array[Char] = getBinary((getDecimal(a) - 1).toShort)
}
