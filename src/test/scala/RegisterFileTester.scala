import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class RegisterFileTester extends AnyFlatSpec
  with ChiselScalatestTester {

  "RegisterFile" should "work correctly" in {
    test(new RegisterFile())
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

        dut.io.writeEnable.poke(false.B)
        dut.io.writeSel.poke(0.U)
        dut.io.writeData.poke(0.U)
        dut.io.aSel.poke(1.U)
        dut.io.bSel.poke(2.U)

        //write 42 in register 1
        dut.io.writeEnable.poke(true.B)
        dut.io.writeSel.poke(1.U)
        dut.io.writeData.poke(42.U)
        dut.clock.step()

        //write 99 in register 2
        dut.io.writeSel.poke(2.U)
        dut.io.writeData.poke(99.U)
        dut.clock.step()

        //read both
        dut.io.writeEnable.poke(false.B)
        dut.io.a.expect(42.U)
        dut.io.b.expect(99.U)

        //try to overwrite register 1 with writing disabled
        dut.io.writeSel.poke(1.U)
        dut.io.writeData.poke(123.U)
        dut.clock.step()
        dut.io.a.expect(42.U)
      }
  }
}