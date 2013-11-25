package com.fantasycpu

package object vm {

  type I = (String, String, String) => Unit
  type S = String

  implicit def from0(f0: () => Unit): I = (_: S, _: S, _: S) => f0()

  implicit def from1(f1: (S) => Unit): I = (p1: S, _: S, _: S) => f1(p1)

  implicit def from2(f2: (S, S) => Unit): I = (p1: S, p2: S, _: S) => f2(p1, p2)

  implicit def from3(f3: (S, S, S) => Unit): I = (p1: S, p2: S, p3: S) => f3(p1, p2, p3)

  implicit def int2Short(i: Int) = i.toShort
}
