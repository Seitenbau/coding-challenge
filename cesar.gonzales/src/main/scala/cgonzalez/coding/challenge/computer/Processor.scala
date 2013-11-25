package cgonzalez.coding.challenge.computer

import cgonzalez.coding.challenge.intructions.instructions._
import cgonzalez.coding.challenge.intructions._

/**
 * Object that represent the Processor
 */
object Processor {

  def apply(instruction: String): Instruction = {
    val tokenizer = instruction.split(Array(' ', ',')).toList.filterNot(_ == "").toArray
    tokenizer.head match {
      //3 Arguments (Char, Char, Char)
      case "ADD" => ADD(tokenizer(1).charAt(0), tokenizer(2).charAt(0), tokenizer(3).charAt(0))
      case "SUB" => SUB(tokenizer(1).charAt(0), tokenizer(2).charAt(0), tokenizer(3).charAt(0))
      //2 Arguments (Char, Long)
      case "AND" => AND(tokenizer(1).charAt(0), tokenizer(2).toLong)
      case "CMP" => CMP(tokenizer(1).charAt(0), tokenizer(2).toLong)
      case "LOAD" => LOAD(tokenizer(1).charAt(0), tokenizer(2).toLong)
      case "LOADM" => LOADM(tokenizer(1).charAt(0), tokenizer(2).toLong)
      case "XOR" => XOR(tokenizer(1).charAt(0), tokenizer(2).toLong)
      case "OR" => OR(tokenizer(1).charAt(0), tokenizer(2).toLong)
      //2 arguments (Long, Char)
      case "SETM" => SETM(tokenizer(1).toInt, tokenizer(2).charAt(0))
      // 1 Argument (Char)
      case "DEC" => DEC(tokenizer(1).charAt(0))
      case "INC" => INC(tokenizer(1).charAt(0))
      case "PRINT" => PRINT(tokenizer(1).charAt(0))
      // 1 Argument (Int)
      case "JNE" => JNE(tokenizer(1).toInt)
      //No Arguments
      case "STOP" => new STOP
      case _ => new Instruction {
        def process: Unit = {
          println("Instruction -> " + instruction + "\n not supported please visit\n  " +
            "http://seitenbau.github.io/coding-challenge/\n  for more information")
        }
      }
    }
  }
}