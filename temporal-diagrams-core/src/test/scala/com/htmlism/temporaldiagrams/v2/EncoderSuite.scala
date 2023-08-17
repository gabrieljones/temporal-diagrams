package com.htmlism.temporaldiagrams.v2

import cats.data._
import cats.syntax.all._
import weaver._

object EncoderSuite extends FunSuite {
  trait TestDsl

  private val intEncoder = new Encoder[TestDsl, Int] {
    def encode(x: Int): NonEmptyList[String] =
      NonEmptyList.one(x.toString)
  }

  test("An encoder can encode, without highlights") {
    expect.eql(NonEmptyList.one("123"), intEncoder.encode(123))
  }

  test("An encoder is contravariant") {
    val stringEncoder =
      intEncoder.contramap((s: String) => s.length)

    expect.eql(NonEmptyList.one("5"), stringEncoder.encode("apple"))
  }
}
