import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class AluTester extends AnyFlatSpec with ChiselScalatestTester {

  "AluTester" should "pass" in {
    test(new ALU())
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

        dut.clock.setTimeout(0)

        //Add 5 and 10
        dut.io.operation.poke(0.U)
        dut.io.left.poke(5.U)
        dut.io.right.poke(10.U)
        dut.clock.step(1)

        //Subtract 7 and 5
        dut.io.operation.poke(1.U)
        dut.io.left.poke(7.U)
        dut.io.right.poke(5.U)
        dut.clock.step(1)

        //Multiply 2 and 3
        dut.io.operation.poke(2.U)
        dut.io.left.poke(2.U)
        dut.io.right.poke(3.U)
        dut.clock.step(1)

        //Multiply 255 and 0
        dut.io.operation.poke(2.U)
        dut.io.left.poke(255.U)
        dut.io.right.poke(0.U)
        dut.clock.step(1)

        //Add 0 and 0
        dut.io.operation.poke(0.U)
        dut.io.left.poke(0.U)
        dut.io.right.poke(0.U)
        dut.clock.step(1)

        //Add 2^31 and 2^16
        dut.io.operation.poke(0.U)
        dut.io.left.poke(2147483648L.U)
        dut.io.right.poke(65536.U)
        dut.clock.step(1)

        //Subtract 5 and 10
        dut.io.operation.poke(1.U)
        dut.io.left.poke(5.U)
        dut.io.right.poke(10.U)
        dut.clock.step(1)

        //Multiply 2^16 and 2^16
        dut.io.operation.poke(2.U)
        dut.io.left.poke(65536.U)
        dut.io.right.poke(65536.U)
        dut.clock.step(1)
    }
  }
}

