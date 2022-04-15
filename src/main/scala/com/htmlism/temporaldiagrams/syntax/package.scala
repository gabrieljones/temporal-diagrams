package com.htmlism.temporaldiagrams

package object syntax {
  implicit class ValueOps[A](x: A) {
    def r: Renderable[A] =
      Renderable.Anonymous(x)

    def id(id: String): Renderable[A] =
      Renderable.ById(id, x)
  }

  implicit class ValueOpsTemporal[A](x: Renderable[A]) {
    def t[K]: Temporal[K, A] =
      Temporal.FixedTemporal(x)
  }

  implicit class RenderableOps[A](r: Renderable[A]) {
    def renderAs[B](implicit B: Dialect[B], enc: DslEncoder[A, B]): String =
      B.consume(enc.encode(r))

    def renderWithHighlightsOn[B](highlights: String*)(implicit B: Dialect[B], enc: DslEncoder[A, B]): String =
      B.consume(enc.encodeWithHighlights(r, highlights.toSet))

    def keys: List[String] =
      Renderable.keys(r)
  }

  implicit class TemporalOps[K: Ordering, A](t: Temporal[K, A]) {
    def at(k: K): Renderable[A] =
      Temporal
        .resolve(t, k)

    def keys: List[K] =
      Temporal
        .keys(t)
  }
}
