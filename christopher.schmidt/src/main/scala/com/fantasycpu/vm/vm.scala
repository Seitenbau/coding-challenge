package com.fantasycpu.vm

import scala.collection.mutable.{HashMap => mHashMap}
import java.nio.{ByteOrder, ByteBuffer}

/**
 * object representing the CPU
 * includes some convenient- and accessor methods to registers and RAM
 */
private[vm] object Registers {
  // Byte Array representing the RAM
  private val ram = Array.fill[Byte](256)(0.toByte)
  // Map of 3 registers A, B and C
  private val register = mHashMap[String, Short]("A" -> 0.toShort, "B" -> 0.toShort, "C" -> 0.toShort)
  // the Z register
  private var Z = false
  // program counter
  private var IP = 0

  /**
   * convenience methods for using registers
   */
  implicit def changeState(r: String) = new {
    /**
     * set register r with value
     * @param value Short register r = value
     */
    def sr(value: Short): Unit = register += r -> value

    /**
     * get value of register r
     * @return register r
     */
    def gr: Short = register(r)

    /**
     * get memory at location r
     * @return Short memory at r
     */
    def gm: Short = {
      val lb = ram(r.toInt)
      val hb = ram(r.toInt + 1)
      ByteBuffer.wrap(Array[Byte](lb, hb)).order(ByteOrder.LITTLE_ENDIAN).getShort
    }

    /**
     * set memory with value
     * @param value Short memory at r = value
     */
    def sm(value: Short): Unit = {
      val bb = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(value)
      bb.flip
      val ar = bb.array
      ram.update(r.toInt, ar(0))
      ram.update(r.toInt + 1, ar(1))
    }

    /**
     * compare register r with a value
     * @param value Short value to use for compare
     */
    def cmp(value: Short): Unit = Z = gr == value

    /**
     * jump to r, if Z == 0
     */
    def jne: Unit = if (!Z) IP = IP + r.toInt else incIP
  }

  /**
   * prints the state of registers
   */
  def printRegisters = {
    println("\n")
    register.keySet.toList.sortWith((s1, s2) => s2 > s1).foreach((p) => println(p + ": " + register(p) + ","))
    println("Z: " + (if (Z) "true" else "false"))
    println("IP: " + IP)
  }

  /**
   * increments the IP by one
   */
  def incIP = IP = IP + 1

  /**
   * accessor to IP
   * @return Int value of the IP
   */
  def gIP = IP

  /**
   * sets Z to 0 (false)
   */
  def resetZ = Z = false

}

private[vm] object JVM {

  import Registers._

  private val microCode = Map[S, I](
    "LOAD" -> ((r: S, v: S) => {
      r.sr(v.toShort)
      incIP
      resetZ
    }),
    "LOADM" -> ((r: S, mem: S) => {
      r.sr(mem.gm)
      incIP
      resetZ
    }),
    "SETM" -> ((mem: S, r: S) => {
      mem.sm(r.gr)
      incIP
      resetZ
    }),
    "ADD" -> ((r: S, r1: S, r2: S) => {
      r.sr(r1.gr + r2.gr)
      incIP
      resetZ
    }),
    "SUB" -> ((r: S, r1: S, r2: S) => {
      r.sr(r1.gr - r2.gr)
      incIP
      resetZ
    }),
    "INC" -> ((r: S) => {
      r.sr(r.gr + 1)
      incIP
      resetZ
    }),
    "DEC" -> ((r: S) => {
      r.sr(r.gr - 1)
      incIP
      resetZ
    }),
    "OR" -> ((r: S, v: S) => {
      r.sr(r.gr | v.toShort)
      incIP
      resetZ
    }),
    "XOR" -> ((r: S, v: S) => {
      r.sr(r.gr ^ v.toShort)
      incIP
      resetZ
    }),
    "AND" -> ((r: S, v: S) => {
      r.sr(r.gr & v.toShort)
      incIP
      resetZ
    }),
    "CMP" -> ((r: S, v: S) => {
      r.cmp(v.toShort)
      incIP
    }),
    "JNE" -> ((rel: S, n: S) => {
      rel.jne
      resetZ
    }),
    "PRINT" -> ((text: S) => {
      print(text.gr.toString)
      incIP
      resetZ
    }),
    "STOP" -> (() => {
      resetZ
      incIP
      printRegisters
      System.exit(0)
    })
  )

  /**
   * runs a program from file
   * @param file String path to program
   */
  def runFromFile(file: String): Unit = {
    val source = io.Source.fromFile(file)
    val code = source.getLines.map(_.split(Array(' ', ',')).filter(_.size != 0)).toArray
    source.close()

    while (true) {
      try {
        code(gIP) match {
          case Array(c: String, p1: String, p2: String, p3: String) => microCode(c)(p1, p2, p3)
          case Array(c: String, p1: String, p2: String) => microCode(c)(p1, p2, null)
          case Array(c: String, p1: String) => microCode(c)(p1, null, null)
          case Array(c: String) => microCode(c)(null, null, null)
        }
      }
      catch {
        case e =>
          microCode("STOP")(null, null, null)
          /*println("\nError at line: " + gIP + ". Code: " +
            (if (code.length > gIP && code(gIP).length > 0) code(gIP)(0) else " none"))
          println("Exception: " + e.printStackTrace())*/
          System.exit(1)
      }
    }
  }
}

/**
 * main "method"
 */
object ExecuteVM extends App {
  if (args.size == 1)
    JVM.runFromFile(args(0))
  else
    println("Execute [program path]")
}