#!/usr/bin/python

import re
import sys


class ParseError(RuntimeError):
    pass


class SFCPU(object):

    def __init__(self):
        self.ram = [0] * 256
        self.reg = {'A': 0, 'B': 0, 'C': 0}
        self.ip = 0
        self.z = 0

    def execute(self, code):
        while True:
            line = code[self.ip]
            m = re.match(r'LOAD\s+(?P<reg>[A-C])\s*,\s*(?P<value>\d+)', line)
            if m:
                value = int(m.group('value'))
                assert value < 65536
                self.reg[m.group('reg')] = value
                self.ip += 1
                self.z = 0
                continue
            m = re.match(r'LOADM\s+(?P<reg>[A-C])\s*,\s*(?P<address>\d+)', line)
            if m:
                address = int(m.group('address'))
                assert address < 255
                self.reg[m.group('reg')] = 256 * self.ram[address] + self.ram[address + 1]
                self.ip += 1
                self.z = 0
                continue
            m = re.match(r'SETM\s+(?P<address>\d+)\s*,\s*(?P<reg>[A-C])', line)
            if m:
                address = int(m.group('address'))
                assert address < 255
                reg = self.reg[m.group('reg')]
                self.ram[address] = reg >> 8
                self.ram[address + 1] = reg & 0xff
                self.ip += 1
                self.z = 0
                continue
            m = re.match(r'ADD\s+(?P<reg1>[A-C])\s*,\s*(?P<reg2>[A-C])\s*,\s*(?P<reg3>[A-C])', line)
            if m:
                self.reg[m.group('reg1')] = (self.reg[m.group('reg2')] + self.reg[m.group('reg3')]) & 0xffff
                self.ip += 1
                self.z = 0
                continue
            m = re.match(r'SUB\s+(?P<reg1>[A-C])\s*,\s*(?P<reg2>[A-C])\s*,\s*(?P<reg3>[A-C])', line)
            if m:
                self.reg[m.group('reg1')] = (0x10000 + self.reg[m.group('reg2')] - self.reg[m.group('reg3')]) & 0xffff
                self.ip += 1
                self.z = 0
                continue
            m = re.match(r'INC\s+(?P<reg>[A-C])', line)
            if m:
                self.reg[m.group('reg')] = (self.reg[m.group('reg')] + 1) & 0xffff
                self.ip += 1
                self.z = 0
                continue
            m = re.match(r'DEC\s+(?P<reg>[A-C])', line)
            if m:
                self.reg[m.group('reg')] = (0x10000 + self.reg[m.group('reg')] - 1) & 0xffff
                self.ip += 1
                self.z = 0
                continue
            m = re.match(r'OR\s+(?P<reg>[A-C])\s*,\s*(?P<value>\d+)', line)
            if m:
                value = int(m.group('value'))
                assert value < 65536
                self.reg[m.group('reg')] |= value
                self.ip += 1
                self.z = 0
                continue
            m = re.match(r'XOR\s+(?P<reg>[A-C])\s*,\s*(?P<value>\d+)', line)
            if m:
                value = int(m.group('value'))
                assert value < 65536
                self.reg[m.group('reg')] ^= value
                self.ip += 1
                self.z = 0
                continue
            m = re.match(r'AND\s+(?P<reg>[A-C])\s*,\s*(?P<value>\d+)', line)
            if m:
                value = int(m.group('value'))
                assert value < 65536
                self.reg[m.group('reg')] &= value
                self.ip += 1
                self.z = 0
                continue
            m = re.match(r'CMP\s+(?P<reg>[A-C])\s*,\s*(?P<value>\d+)', line)
            if m:
                value = int(m.group('value'))
                assert value < 65536
                self.z = 1 if self.reg[m.group('reg')] == value else 0
                self.ip += 1
                continue
            m = re.match(r'JNE\s+(?P<offset>-?\d+)', line)
            if m:
                offset = int(m.group('offset'))
                assert 0 <= self.ip + offset < len(code)
                self.ip += offset if self.z == 0 else 1
                self.z = 0
                continue
            m = re.match(r'PRINT\s+(?P<reg>[A-C])', line)
            if m:
                sys.stdout.write(str(self.reg[m.group('reg')]))
                self.ip += 1
                self.z = 0
                continue
            m = re.match(r'STOP', line)
            if m:
                break
            raise ParseError('cannot parse %r' % (line,))
        print
        print 'A: %d,' % (self.reg['A'],)
        print 'B: %d,' % (self.reg['B'],)
        print 'C: %d,' % (self.reg['C'],)
        print 'Z: %d,' % (self.z,)
        print 'IP: %d' % (self.ip,)


if __name__ == '__main__':
    try:
        sfcpu = SFCPU()
        sfcpu.execute(open(sys.argv[1]).readlines())
        sys.exit(0)
    except ParseError, e:
        print e
        sys.exit(1)
