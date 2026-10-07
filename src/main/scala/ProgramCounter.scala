import chisel3._
import chisel3.util._

class ProgramCounter extends Module {
  val io = IO(new Bundle {
    val stop = Input(Bool())
    val jump = Input(Bool())
    val run = Input(Bool())
    val programCounterJump = Input(UInt(16.W))
    val programCounter = Output(UInt(16.W))
  })

  //Implement this module here (respect the provided interface, since it used by the tester)
  val memory = Mem (1 , UInt (16.W))

  io.programCounter := memory.read(0.U)
  when (io.run) {
    when (!io.stop) {
      when (io.jump) {
        memory.write(0.U, io.programCounterJump)
        io.programCounter := memory.read(0.U)
      } .otherwise {
        memory.write(0.U, memory.read(0.U) + 1.U)
        io.programCounter := memory.read(0.U)
      }
    }
  }
}