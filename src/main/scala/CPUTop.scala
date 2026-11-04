import chisel3._
import chisel3.util._

class CPUTop extends Module {
  val io = IO(new Bundle {
    val done = Output(Bool ())
    val run = Input(Bool ())
    //This signals are used by the tester for loading and dumping the memory content, do not touch
    val testerDataMemEnable = Input(Bool ())
    val testerDataMemAddress = Input(UInt (16.W))
    val testerDataMemDataRead = Output(UInt (32.W))
    val testerDataMemWriteEnable = Input(Bool ())
    val testerDataMemDataWrite = Input(UInt (32.W))
    //This signals are used by the tester for loading and dumping the memory content, do not touch
    val testerProgMemEnable = Input(Bool ())
    val testerProgMemAddress = Input(UInt (16.W))
    val testerProgMemDataRead = Output(UInt (32.W))
    val testerProgMemWriteEnable = Input(Bool ())
    val testerProgMemDataWrite = Input(UInt (32.W))
  })

  //Creating components
  val programCounter = Module(new ProgramCounter())
  val dataMemory = Module(new DataMemory())
  val programMemory = Module(new ProgramMemory())
  val registerFile = Module(new RegisterFile())
  val controlUnit = Module(new ControlUnit())
  val alu = Module(new ALU())

  //ProgramMemory
  programMemory.io.address := programCounter.io.programCounter

  //ControlUnit
  controlUnit.io.memoryInput := programMemory.io.instructionRead

  //ProgramCounter
  programCounter.io.stop := controlUnit.io.stop
  programCounter.io.run := io.run
  programCounter.io.programCounterJump := controlUnit.io.data
  programCounter.io.jump :=
    controlUnit.io.jump ||
    (controlUnit.io.jumpEquals && alu.io.isZero) ||
    (controlUnit.io.jumpLessThan && !alu.io.isZero && !alu.io.overflow)

  //DataMemory
  dataMemory.io.address := registerFile.io.b
  dataMemory.io.writeEnable := controlUnit.io.dataWriteEnable
  dataMemory.io.dataWrite := registerFile.io.a

  //registerFile
  registerFile.io.aSel :=  controlUnit.io.regA
  registerFile.io.bSel := controlUnit.io.regB
  registerFile.io.writeSel := controlUnit.io.regC
  registerFile.io.writeEnable := controlUnit.io.regCIn
  registerFile.io.writeData := MuxCase(0.U, Seq(
    (controlUnit.io.registerInputSelect === regInputSelect.DIRECT) -> controlUnit.io.data,
    (controlUnit.io.registerInputSelect === regInputSelect.ALU) -> alu.io.result,
    (controlUnit.io.registerInputSelect === regInputSelect.MEMORY) -> dataMemory.io.dataRead
  ))

  //ALU
  alu.io.operation := controlUnit.io.operation
  alu.io.left := registerFile.io.a
  alu.io.right := Mux(controlUnit.io.aluDirect, controlUnit.io.data, registerFile.io.b)

  ////////////////////////////////////////////
  //Continue here with your connections
  ////////////////////////////////////////////

  //This signals are used by the tester for loading the program to the program memory, do not touch
  programMemory.io.testerAddress := io.testerProgMemAddress
  io.testerProgMemDataRead := programMemory.io.testerDataRead
  programMemory.io.testerDataWrite := io.testerProgMemDataWrite
  programMemory.io.testerEnable := io.testerProgMemEnable
  programMemory.io.testerWriteEnable := io.testerProgMemWriteEnable
  //This signals are used by the tester for loading and dumping the data memory content, do not touch
  dataMemory.io.testerAddress := io.testerDataMemAddress
  io.testerDataMemDataRead := dataMemory.io.testerDataRead
  dataMemory.io.testerDataWrite := io.testerDataMemDataWrite
  dataMemory.io.testerEnable := io.testerDataMemEnable
  dataMemory.io.testerWriteEnable := io.testerDataMemWriteEnable
}