local lpeg = require "lpeg"
local io = require "io"
local string = require "string"
local bit = require "bit"

-- bitwise operations shortcuts
local bnot = bit.bnot
local band, bor, bxor = bit.band, bit.bor, bit.bxor
local lshift, rshift, rol = bit.lshift, bit.rshift, bit.rol

-- lpeg patterns
local Space = lpeg.S(" \t")^0
local PositiveNumber = lpeg.R("09")^1
local Number = lpeg.P("-")^-1 * PositiveNumber

local RegisterA = lpeg.P("A")
local RegisterB = lpeg.P("B")
local RegisterC = lpeg.P("C")
local RegisterZ = lpeg.P("Z")
local RegisterIP = lpeg.P("IP")
local GeneralPurposeRegisters = RegisterA + RegisterB + RegisterC
local ModifiableRegisters = GeneralPurposeRegisters + RegisterIP
local AllRegisters = ModifiableRegisters + RegisterZ

local Comma = lpeg.P(",")
local CommandLoadm = lpeg.C(lpeg.P("LOADM")) * Space * lpeg.C(ModifiableRegisters) * Space * Comma * Space * lpeg.C(PositiveNumber)
local CommandLoad = lpeg.C(lpeg.P("LOAD")) * Space * lpeg.C(ModifiableRegisters) * Space * Comma * Space * lpeg.C(Number)
local CommandSetm = lpeg.C(lpeg.P("SETM")) * Space * lpeg.C(PositiveNumber) * Space * Comma * Space * lpeg.C(AllRegisters)
local CommandAdd = lpeg.C(lpeg.P("ADD")) * Space * lpeg.C(ModifiableRegisters) * Space * Comma * Space * lpeg.C(AllRegisters) * Space * lpeg.C(AllRegisters)
local CommandSub = lpeg.C(lpeg.P("SUB")) * Space * lpeg.C(ModifiableRegisters) * Space * Comma * Space * lpeg.C(AllRegisters) * Space * lpeg.C(AllRegisters)
local CommandInc = lpeg.C(lpeg.P("INC")) * Space * lpeg.C(ModifiableRegisters)
local CommandDec = lpeg.C(lpeg.P("DEC")) * Space * lpeg.C(ModifiableRegisters)
local CommandOr = lpeg.C(lpeg.P("OR")) * Space * lpeg.C(ModifiableRegisters) * Space * Comma * Space * lpeg.C(Number)
local CommandXor = lpeg.C(lpeg.P("XOR")) * Space * lpeg.C(ModifiableRegisters) * Space * Comma * Space * lpeg.C(Number)
local CommandAnd = lpeg.C(lpeg.P("AND")) * Space * lpeg.C(ModifiableRegisters) * Space * Comma * Space * lpeg.C(Number)
local CommandCmp = lpeg.C(lpeg.P("CMP")) * Space * lpeg.C(ModifiableRegisters) * Space * Comma * Space * lpeg.C(Number)
local CommandJne = lpeg.C(lpeg.P("JNE")) * Space * lpeg.C(Number)
local CommandPrint = lpeg.C(lpeg.P("PRINT")) * Space * lpeg.C(AllRegisters)
local CommandStop = lpeg.C(lpeg.P("STOP"))

local Command = CommandLoadm + CommandLoad + CommandSetm + CommandAdd + CommandSub + CommandInc + CommandDec + CommandOr + CommandXor + CommandAnd + CommandCmp + CommandJne + CommandPrint + CommandStop

local G = lpeg.Ct(Command)

-- machine RAM
local ram = {}
for i=1,256 do
	ram[i] = 255
end
-- machine registers
local registernames = {"A", "B", "C", "Z", "IP"}
local registers = {["A"] = 0, ["B"] = 0, ["C"] = 0, ["Z"] = 0, ["IP"] = 0}

-- commands
local function loadm(args)
	-- register, address
	local reg = args[2]
	local adr = tonumber(args[3])
	local firstByte = band(ram[adr+1], 255)
	local secondByte =  band(ram[adr+2], 255)
	registers[reg] = firstByte + lshift(secondByte, 8)
	-- registers["Z"] = 0
	registers["IP"] = registers["IP"] + 1
end

local function load(args)
	-- register, address
	local reg = args[2]
	local val = tonumber(args[3])
	registers[reg] = band(val, 0xffff)
	-- registers["Z"] = 0
	registers["IP"] = registers["IP"] + 1
end

local function setm(args)
	-- register, address
	local adr = tonumber(args[2])
	local reg = args[3]
	local firstByte = band(registers[reg], 255)
	local secondByte =  band(rshift(registers[reg], 8), 255)
	ram[adr+1] = firstByte
	ram[adr+2] = secondByte
	-- registers["Z"] = 0
	registers["IP"] = registers["IP"] + 1
end

local function add(args)
	-- register, register register
	local reg1 = args[2]
	local reg2 = args[3]
	local reg3 = args[4]
	registers[reg1] = registers[reg2] + registers[reg3]
	-- registers["Z"] = 0
	registers["IP"] = registers["IP"] + 1
end

local function sub(args)
	-- register, register register
	local reg1 = args[2]
	local reg2 = args[3]
	local reg3 = args[4]
	registers[reg1] = registers[reg2] - registers[reg3]
	-- registers["Z"] = 0
	registers["IP"] = registers["IP"] + 1
end

local function inc(args)
	-- register
	local reg = args[2]
	registers[reg] = registers[reg] + 1
	-- registers["Z"] = 0
	registers["IP"] = registers["IP"] + 1
end

local function dec(args)
	-- register
	local reg = args[2]
	registers[reg] = registers[reg] - 1
	-- registers["Z"] = 0
	registers["IP"] = registers["IP"] + 1
end

local function cor(args)
	-- register, value
	local reg = args[2]
	local val = tonumber(args[3])
	registers[reg] = bor(registers[reg], val)
	-- registers["Z"] = 0
	registers["IP"] = registers["IP"] + 1
end

local function xor(args)
	-- register, value
	local reg = args[2]
	local val = tonumber(args[3])
	registers[reg] = bxor(registers[reg], val)
	-- registers["Z"] = 0
	registers["IP"] = registers["IP"] + 1
end

local function cand(args)
	-- register, value
	local reg = args[2]
	local val = tonumber(args[3])
	registers[reg] = band(registers[reg], val)
	-- registers["Z"] = 0
	registers["IP"] = registers["IP"] + 1
end

local function cmp(args)
	-- register, value
	local reg = args[2]
	local val = tonumber(args[3])
	if registers[reg] == val then
		registers["Z"] = 1
	else
		registers["Z"] = 0
	end
	registers["IP"] = registers["IP"] + 1
end

local function jne(args)
	-- offset
	local val = tonumber(args[2])
	if registers["Z"] == 0 then
		registers["IP"] = registers["IP"] + val
	else
		registers["IP"] = registers["IP"] + 1
	end
	-- registers["Z"] = 0
end

local function cprint(args)
	-- register
	local reg = args[2]
	io.write(string.format("%d",registers[reg]))
	-- registers["Z"] = 0
	registers["IP"] = registers["IP"] + 1
end

local function stop(args)
	registers["IP"] = registers["IP"] + 1
	print("")
	print("")
	for i, value in ipairs(registernames) do
		if value=="Z" then
			if registers["Z"]==0 then
				print("Z: false")
			else
				print("Z: true")
			end
		else
			print(value..": "..registers[value])
		end
	end
end

local commands = {["LOADM"] = loadm, ["LOAD"] = load, ["SETM"] = setm, ["ADD"] = add, ["SUB"] = sub, ["DEC"] = dec, ["INC"] = inc, ["OR"] = cor, ["XOR"] = xor, ["AND"] = cand, ["CMP"] = cmp, ["JNE"] = jne, ["PRINT"] = cprint, ["STOP"] = stop}

-- debug output
local function output()
	print("REGS:")
	for key, value in pairs(registers) do
		if key=="Z" then
			if value==0 then
				print("Z: false")
			else
				print("Z: true")
			end
		else
			print(key..": "..value)
		end
	end
	print("MEM:")
	for i, v in ipairs(ram) do
		io.write(bit.tohex(ram[i], 2))
	end
	print("")
end

local function main(fname)
	io.input(fname)
	local lines = {}
	while true do
		local line = io.read()
		if line == nil then break end
		table.insert(lines, line)
	end
	while true do
		local line
		if registers["IP"] < #lines then
			line = lines[registers["IP"]+1]
		else
			line = "STOP"
		end
		local t = lpeg.match(G, line)
		if not t then os.exit(1) end
		commands[t[1]](t)
		-- output()
		if t[1] == "STOP" then break end
    end
end
if not arg[1] then
	os.exit(1)
end
main(arg[1])
