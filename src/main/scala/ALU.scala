import chisel3._
import chisel3.util._

object operations {
  val NOP = 0.U(3.W)
  val ADD = 1.U(3.W)
  val SUB = 2.U(3.W)
  val MULT = 3.U(3.W)
  val OR = 4.U(3.W)
  val AND =5.U(3.W)
  val NOT = 6.U(3.W)
}

class ALU extends Module {
  val io = IO(new Bundle {
    //Define the module interface here (inputs/outputs)
    val operation = Input(UInt(3.W))
    val left = Input(UInt(32.W))
    val right = Input(UInt(32.W))

    val result = Output(UInt(32.W))
    val isZero = Output(Bool())
    val overflow = Output(Bool())
  })

  //Implement this module here
  io.overflow := false.B
  io.result := 0.U

  switch(io.operation) {
    is(operations.ADD) {
      io.result := io.left + io.right
      io.overflow := (io.left +& io.right)(32)
    }
    is(operations.SUB) {
      io.result := io.left - io.right
      io.overflow := (io.left -& io.right)(32)
    }
    is(operations.MULT) {
      io.result := io.left * io.right
    }
    is(operations.OR) { io.result := io.left | io.right }
    is(operations.AND) { io.result := io.left & io.right }
    is(operations.NOT) { io.result := ~io.left }
  }

  io.isZero := (io.result === 0.U)
}