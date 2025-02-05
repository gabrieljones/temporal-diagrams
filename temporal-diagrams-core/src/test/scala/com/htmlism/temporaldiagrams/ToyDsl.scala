package com.htmlism.temporaldiagrams

import cats.syntax.all.*

import com.htmlism.temporaldiagrams.syntax.*

sealed trait ToyDsl

case class Service(name: String, dependency: Option[String]) extends ToyDsl

object Service:
  import PlantUml.*

  given DslEncoder[Service, PlantUml] with
    def encodeWithHighlights(x: Service, highlighted: Boolean): List[PlantUml] =
      if highlighted then renderFlatMonoid(x, "Service".some)
      else renderFlatMonoid(x, None)

    def encode(x: Service): List[PlantUml] =
      renderFlatMonoid(x, "Service".some)

    def renderArrow(src: String, dest: String): List[Renderable.Tagged[Service]] =
      List(
        Service(s"$src to $dest").r
      )

    def debug(xs: List[String]): List[PlantUml] =
      Nil

    private def renderFlatMonoid(x: Service, tag: Option[String]) =
      val component =
        List(Component(x.name, None, tag))

      val dependency =
        x.dependency.toList.map(Link(_, x.name))

      component ::: dependency

  def apply(s: String): Service =
    Service(s, None)
