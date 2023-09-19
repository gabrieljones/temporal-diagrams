package com.htmlism.temporaldiagrams.v2
package syntax

import scala.collection.immutable.ListSet

import cats.data.NonEmptyList
import weaver._

object SyntaxSuite extends FunSuite {
  test("Domain objects from unrelated hierarchies can be bound together, with postfix syntax") {
    val implicitRs =
      NonEmptyList.of[Renderable[NonEmptyList[ToyDiagramLanguage]]](
        Amazon.Ec2("").r,
        Google.Compute("").r
      )

    expect.same(RenderableSuite.explicitRs, implicitRs)
  }

  test("Domain objects from unrelated hierarchies can be bound together, with postfix tagging") {
    val tagged =
      NonEmptyList.of[Renderable[NonEmptyList[ToyDiagramLanguage]]](
        Amazon.Ec2("").tag("hello"),
        Google.Compute("")
      )

    expect.same(ListSet("hello"), tagged.head.tags) &&
    expect.same(ListSet.empty, tagged.toList(1).tags)
  }

  test("Domain objects can be lifted implicitly") {
    val implicitRs =
      NonEmptyList.of[Renderable[NonEmptyList[ToyDiagramLanguage]]](
        Amazon.Ec2(""),
        Google.Compute("")
      )

    expect.same(RenderableSuite.explicitRs, implicitRs)
  }
}
