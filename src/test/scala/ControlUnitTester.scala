import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class ControlUnitTester extends AnyFlatSpec with ChiselScalatestTester {

  "ControlUnitTester" should "pass" in {
    test(new ControlUnit())
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

        dut.clock.setTimeout(0)

          //ADD 0 1 2
          dut.io.memoryInput.poke(0b000001_000_001_010_0_0000_0000_0000_0000.U)
          dut.clock.step(1)

          //SUB 2 1 0
          dut.io.memoryInput.poke(0b000010_010_001_000_0_0000_0000_0000_0000.U)
          dut.clock.step(1)

          //JMP somenumber
          dut.io.memoryInput.poke(0b010000_000_000_000_0_0000_0010_0110_0000.U)
          dut.clock.step(1)
    }
  }
}

