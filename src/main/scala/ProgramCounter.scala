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
  val counter = RegInit(0.U(16.W))

  counter := Mux(!io.run || io.stop, counter, Mux(io.jump, io.programCounterJump, counter + 1.U))
  io.programCounter := counter
}