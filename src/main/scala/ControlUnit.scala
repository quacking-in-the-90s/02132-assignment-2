import chisel3._
import chisel3.util._

object regInputSelect {
  val NONE = 0.U(2.W)
  val DIRECT = 1.U(2.W)
  val ALU = 2.U(2.W)
  val MEMORY = 3.U(2.W)
}

object opcodes {
  val ADD  = 0b000001.U
  val SUB  = 0b000010.U
  val MULT = 0b000011.U
  val ADDI = 0b000100.U
  val SUBI = 0b000101.U
  val OR   = 0b000110.U
  val AND  = 0b000111.U
  val NOT  = 0b001000.U
  val LI   = 0b001001.U
  val LD   = 0b001010.U
  val SD   = 0b001011.U
  val JMP  = 0b010000.U
  val JEQ  = 0b010001.U
  val JLT  = 0b010010.U
  val JGT  = 0b010011.U
  val NOP  = 0b000000.U
  val END  = 0b111111.U
}

class ControlUnit extends Module {
  val io = IO(new Bundle {
    //Define the module interface here (inputs/outputs)
    val memoryInput = Input(UInt(32.W))

    val data = Output(UInt(16.W))

    //Registers
    //read
    val regA = Output(UInt(3.W))
    val regB = Output(UInt(3.W))
    //write
    val regC = Output(UInt(3.W))
    val regCIn = Output(Bool())
    val registerInputSelect = Output(UInt(2.W))

    //ProgramCounter
    val stop = Output(Bool())
    val jump = Output(Bool())
    val jumpEquals = Output(Bool())
    val jumpLessThan = Output(Bool())

    //Alu
    val operation = Output(UInt(3.W))
    val aluDirect = Output(Bool())

    //DataMemory
    val dataWriteEnable = Output(Bool())
  })

  io.regCIn := false.B
  io.stop := false.B
  io.dataWriteEnable := false.B
  io.aluDirect := false.B
  io.regA := 0.U
  io.regB := 0.U
  io.regC := 0.U
  io.operation := operations.NOP
  io.registerInputSelect := regInputSelect.NONE
  io.data := 0.U
  io.jump := false.B
  io.jumpEquals := false.B
  io.jumpLessThan := false.B

  val opcode = io.memoryInput(31,26)
  val R1 = io.memoryInput(25,23)
  val R2 = io.memoryInput(22,20)
  val R3 = io.memoryInput(19,17)
  val data = io.memoryInput(15,0)

  switch(opcode) {
    is(opcodes.NOP) { }
    is(opcodes.ADD) {
      io.regCIn := true.B
      io.operation := operations.ADD
      io.regA := R2
      io.regB := R3
      io.regC := R1
      io.registerInputSelect := regInputSelect.ALU
    }
    is(opcodes.SUB) {
      io.regCIn := true.B
      io.operation := operations.SUB
      io.regA := R2
      io.regB := R3
      io.regC := R1
      io.registerInputSelect := regInputSelect.ALU
    }
    is(opcodes.MULT) {
      io.regCIn := true.B
      io.operation := operations.MULT
      io.regA := R2
      io.regB := R3
      io.regC := R1
      io.registerInputSelect := regInputSelect.ALU

    }
    is(opcodes.ADDI) {
      io.regCIn := true.B
      io.operation := operations.ADD
      io.regA := R2
      io.regC := R1
      io.data := data
      io.aluDirect := true.B
      io.registerInputSelect := regInputSelect.ALU
    }
    is(opcodes.SUBI) {
      io.regCIn := true.B
      io.operation := operations.SUB
      io.regA := R2
      io.regC := R1
      io.data := data
      io.aluDirect := true.B
      io.registerInputSelect := regInputSelect.ALU
    }
    is(opcodes.OR) {
      io.regCIn := true.B
      io.operation := operations.OR
      io.regA := R2
      io.regB := R3
      io.regC := R1
      io.registerInputSelect := regInputSelect.ALU
    }
    is(opcodes.AND) {
      io.regCIn := true.B
      io.operation := operations.AND
      io.regA := R2
      io.regB := R3
      io.regC := R1
      io.registerInputSelect := regInputSelect.ALU
    }
    is(opcodes.NOT) {
      io.regCIn := true.B
      io.operation := operations.NOT
      io.regA := R2
      io.regC := R1
      io.registerInputSelect := regInputSelect.ALU
    }
    is(opcodes.LI) {
      io.regCIn := true.B
      io.regC := R1
      io.data := data
      io.registerInputSelect := regInputSelect.DIRECT
    }
    is(opcodes.LD) {
      io.regB := R2
      io.regCIn := true.B
      io.regC := R1
      io.registerInputSelect := regInputSelect.MEMORY
    }
    is(opcodes.SD) {
      io.regA := R1
      io.regB := R2
      io.dataWriteEnable := true.B
    }

    is(opcodes.JMP) {
      io.jump := true.B
      io.data := data
    }

    is(opcodes.JEQ) {
      io.jumpEquals := true.B
      io.data := data
      io.regA := R1
      io.regB := R2
      io.operation := operations.SUB
    }
    is(opcodes.JLT) {
      io.jumpLessThan := true.B
      io.data := data
      io.regA := R1
      io.regB := R2
      io.operation := operations.SUB
    }
    is(opcodes.JGT) {
      io.jumpLessThan := true.B
      io.data := data
      io.regA := R2
      io.regB := R1
      io.operation := operations.SUB
    }
    is(opcodes.END) {
      io.stop := true.B
    }
  }
}