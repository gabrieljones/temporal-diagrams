package com.htmlism.temporaldiagrams.v2

import scala.collection.immutable.ListSet

package object syntax:

  /**
    * Postfix syntax enhancement for marking an expression as renderable
    *
    * @param x
    *   The data being rendered
    * @tparam A
    *   The type of the data being rendered
    */
  extension [A](x: A)

    /**
      * Marks an expression as renderable to `D`, without any tags
      *
      * @tparam D
      *   The target diagram language
      */
    def r[D](using enc: HighlightEncoder[D, A]): Renderable.OfA[A, D] =
      Renderable.OfA(x, ListSet.empty)

    /**
      * Marks an expression as renderable to `D`, with the specified tags
      *
      * @tparam D
      *   The target diagram language
      *
      * @param t
      *   A required tag
      * @param ts
      *   Optional, additional tags
      */
    def tag[D](t: String, ts: String*)(using enc: HighlightEncoder[D, A]): Renderable.OfA[A, D] =
      Renderable.OfA(x, ListSet.from(t +: ts))
