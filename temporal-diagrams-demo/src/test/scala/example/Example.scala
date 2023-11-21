package example

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should

class Example extends AnyFunSuite with should.Matchers:
  test("example"):
    "abbccxxx" should startWith regex ("a(b*)(c*)" withGroups("bb", "cc"))
    "xxxabbcc" should endWith regex ("a(b*)(c*)" withGroups("bb", "cc"))
    "xxxabbccxxx" should include regex ("a(b*)(c*)" withGroups("bb", "cc"))
    "abbcc" should fullyMatch regex ("a(b*)(c*)" withGroups("bb", "cc"))
