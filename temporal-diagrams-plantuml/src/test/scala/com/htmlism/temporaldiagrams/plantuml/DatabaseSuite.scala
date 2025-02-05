package com.htmlism.temporaldiagrams.plantuml

import cats.data.*
import cats.syntax.all.*
import weaver.*

import com.htmlism.temporaldiagrams.v2.DiagramEncoder

object DatabaseSuite extends FunSuite:
  test("A database has an name"):
    expect.eql(
      Chain("database asdf"),
      DiagramEncoder[PlantUml].encode(
        PlantUml.Database("asdf", None, None, Nil)
      )
    )

  test("A database has an optional override alias"):
    expect.eql(
      Chain("database foo as bar"),
      DiagramEncoder[PlantUml].encode(
        PlantUml.Database("foo", "bar".some, None, Nil)
      )
    )

  test("A database has an optional stereotype"):
    expect.eql(
      Chain("database foo << bar >>"),
      DiagramEncoder[PlantUml].encode(
        PlantUml.Database("foo", None, "bar".some, Nil)
      )
    )

  // TODO test nesting
