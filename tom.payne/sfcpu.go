// FIXME commas in output?
// FIXME number of leading newlines in output?
// FIXME signed/unsigned operations

package main

import (
	"errors"
	"fmt"
	"os"
)

const (
	A = 0
	B = 1
	C = 2
)

var (
	regNames = []string{"A", "B", "C"}
)

var (
	ErrSegv = errors.New("SEGV")
	ErrStop = errors.New("STOP")
)

type SFCPU struct {
	ram  [256]byte
	reg  [3]uint16
	ip   int
	z    int
}

type Instruction interface {
	fmt.Stringer
	Execute(*SFCPU) error
}

type LOAD struct {
	reg   int
	value uint16
}

type LOADM struct {
	reg     int
	address byte
}

type SETM struct {
	address byte
	reg     int
}

type ADD struct {
	reg1 int
	reg2 int
	reg3 int
}

type SUB struct {
	reg1 int
	reg2 int
	reg3 int
}

type INC struct {
	reg int
}

type DEC struct {
	reg int
}

type OR struct {
	reg   int
	value uint16
}

type XOR struct {
	reg   int
	value uint16
}

type AND struct {
	reg   int
	value uint16
}

type CMP struct {
	reg   int
	value uint16
}

type JNE struct {
	offset int
}

type PRINT struct {
	reg int
}

type STOP struct {
}

func (i LOAD) Execute(s *SFCPU) error {
	s.reg[i.reg] = i.value
	s.ip++
	s.z = 0
	return nil
}

func (i LOAD) String() string {
	return fmt.Sprintf("LOAD %s,%d", regNames[i.reg], i.value)
}

func (i LOADM) Execute(s *SFCPU) error {
	s.reg[i.reg] = uint16(s.ram[i.address])<<8 + uint16(s.ram[i.address+1])
	s.ip++
	s.z = 0
	return nil
}

func (i LOADM) String() string {
	return fmt.Sprintf("LOADM %s,%d", regNames[i.reg], i.address)
}

func (i SETM) Execute(s *SFCPU) error {
	s.ram[i.address] = byte(s.reg[i.reg] >> 8)
	s.ram[i.address+1] = byte(s.reg[i.reg])
	s.ip++
	s.z = 0
	return nil
}

func (i SETM) String() string {
	return fmt.Sprintf("SETM %d,%s", i.address, regNames[i.reg])
}

func (i ADD) Execute(s *SFCPU) error {
	s.reg[i.reg1] = s.reg[i.reg2] + s.reg[i.reg3]
	s.ip++
	s.z = 0
	return nil
}

func (i ADD) String() string {
	return fmt.Sprintf("ADD %s,%s,%s", regNames[i.reg1], regNames[i.reg2], regNames[i.reg3])
}

func (i SUB) Execute(s *SFCPU) error {
	s.reg[i.reg1] = s.reg[i.reg2] + s.reg[i.reg3]
	s.ip++
	s.z = 0
	return nil
}

func (i SUB) String() string {
	return fmt.Sprintf("SUB %s,%s,%s", regNames[i.reg1], regNames[i.reg2], regNames[i.reg3])
}

func (i INC) Execute(s *SFCPU) error {
	s.reg[i.reg]++
	s.ip++
	s.z = 0
	return nil
}

func (i INC) String() string {
	return fmt.Sprintf("INC %s", regNames[i.reg])
}

func (i DEC) Execute(s *SFCPU) error {
	s.reg[i.reg]--
	s.ip++
	s.z = 0
	return nil
}

func (i DEC) String() string {
	return fmt.Sprintf("DEC %s", regNames[i.reg])
}

func (i OR) Execute(s *SFCPU) error {
	s.reg[i.reg] |= i.value
	s.ip++
	s.z = 0
	return nil
}

func (i OR) String() string {
	return fmt.Sprintf("OR %s,%d", regNames[i.reg], i.value)
}

func (i XOR) Execute(s *SFCPU) error {
	s.reg[i.reg] ^= i.value
	s.ip++
	s.z = 0
	return nil
}

func (i XOR) String() string {
	return fmt.Sprintf("XOR %s,%d", regNames[i.reg], i.value)
}

func (i AND) Execute(s *SFCPU) error {
	s.reg[i.reg] &= i.value
	s.ip++
	s.z = 0
	return nil
}

func (i AND) String() string {
	return fmt.Sprintf("AND %s,%d", regNames[i.reg], i.value)
}

func (i CMP) Execute(s *SFCPU) error {
	if s.reg[i.reg] == i.value {
		s.z = 1
	} else {
		s.z = 0
	}
	s.ip++
	return nil
}

func (i CMP) String() string {
	return fmt.Sprintf("CMP %s,%d", regNames[i.reg], i.value)
}

func (i JNE) Execute(s *SFCPU) error {
	if s.z == 0 {
		s.ip += i.offset
	} else {
		s.ip++
	}
	s.z = 0
	return nil
}

func (i JNE) String() string {
	return fmt.Sprintf("JNE %d", i.offset)
}

func (i PRINT) Execute(s *SFCPU) error {
	fmt.Printf("%d", s.reg[i.reg])
	s.ip++
	s.z = 0
	return nil
}

func (i PRINT) String() string {
	return fmt.Sprintf("PRINT %s", regNames[i.reg])
}

func (i STOP) Execute(s *SFCPU) error {
	s.z = 0
	fmt.Printf("\n\nA: %d,\nB: %d,\nC: %d,\nZ: %d\nIP: %d\n", s.reg[A], s.reg[B], s.reg[C], s.z, s.ip)
	return ErrStop
}

func (i STOP) String() string {
	return "STOP"
}

func (s *SFCPU) Run(instructions []Instruction) error {
	var err error
	for err = instructions[s.ip].Execute(s); err == nil {
	}
	if err == ErrStop {
		return nil
	} else {
		return err
	}
}

func main() {
	instructions := []Instruction{
		LOAD{A, 1},
		SETM{0, A},
		LOADM{A, 0},
		OR{A, 8},
		PRINT{A},
		XOR{A, 42},
		PRINT{A},
		XOR{A, 42},
		PRINT{A},
		LOAD{C, 10},
		PRINT{C},
		DEC{C},
		CMP{C, 0},
		JNE{-3},
		STOP{},
	}
	s := &SFCPU{}
	if err := s.Run(instructions); err == nil {
	}
	os.Exit(0)
}
