import chisel3._
import chisel3.util._

class ALU extends Module {
  val io = IO(new Bundle {
    //Define the module interface here (inputs/outputs)
    val operation = Input(UInt(2.W))
    val left = Input(UInt(32.W))
    val right = Input(UInt(32.W))

    val result = Output(UInt(32.W))
    val isZero = Output(Bool())
    val overflow = Output(Bool())
  })

  //Implement this module here
  io.overflow := false.B;

  when (io.operation === 0.U) {
    io.result := io.left + io.right
  } .elsewhen(io.operation === 1.U) {
    io.result := io.left - io.right
  } .elsewhen(io.operation === 2.U) {
    io.result := io.left * io.right
  } .otherwise {
    io.result := 0.U
  }

  io.isZero := Mux(io.result === 0.U, true.B, false.B)
}