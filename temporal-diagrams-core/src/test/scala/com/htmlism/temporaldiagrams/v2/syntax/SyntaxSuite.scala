package com.htmlism.temporaldiagrams.v2
package syntax

import cats.data.NonEmptyList
import weaver._

object SyntaxSuite extends FunSuite {
  test("Domain objects from unrelated hierarchies can be bound together, implicitly") {
    val implicitRs =
      NonEmptyList.of[Renderable[NonEmptyList[ToyDiagramLanguage]]](
        Amazon.Ec2("").r,
        Google.Compute("").r
      )

    expect.same(RenderableSuite.explicitRs, implicitRs)
  }
}
