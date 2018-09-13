
import org.scalatest.FunSuite

class StarterTest extends FunSuite {
  test("Starter") {
    assert(Starter.getListOfAllowedFiles(List(""),_=>false) === Nil)
  }
}
