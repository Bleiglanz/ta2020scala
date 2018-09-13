
import org.scalatest.FunSuite

class StarterTest extends FunSuite {
  test("Starter") {
    assert(Starter.getListOfAllowedFiles("",_=>false) === Nil)
  }
}
