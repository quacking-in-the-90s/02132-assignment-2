import chisel3._
import chisel3.util._

class RegisterFile extends Module {
  val io = IO(new Bundle {
    val aSel = Input(UInt(4.W))
    val bSel = Input(UInt(4.W))
    val a = Output(UInt(32.W))
    val b = Output(UInt(32.W))

    val writeSel = Input(UInt(4.W))
    val writeData = Input(UInt(32.W))
    val writeEnable = Input(Bool())
  })
  //16 reg, 32 bit wide, resets to zero
  val registers = RegInit(VecInit(Seq.fill(16)(0.U(32.W))))

  //read ports
  io.a := registers(io.aSel)
  io.b := registers(io.bSel)

  //write port
  when(io.writeEnable) {
    registers(io.writeSel) := io.writeData
  }
}