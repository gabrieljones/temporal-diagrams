package com.htmlism.temporaldiagrams.demo.v2

import cats._
import cats.data.Kleisli
import cats.data.NonEmptyList
import cats.effect._
import cats.effect.std.Console

import com.htmlism.temporaldiagrams.plantuml.PlantUml
import com.htmlism.temporaldiagrams.v2.Renderable
import com.htmlism.temporaldiagrams.v2.syntax._

object Demo extends Demo[IO] with IOApp.Simple {
  sealed trait BarAppearance

  object BarAppearance {
    case object AsService extends BarAppearance

    case object AsHydra extends BarAppearance

    case object WithBuffer extends BarAppearance
  }

  case class ConfigBasket(isNew: Boolean, barStyle: BarAppearance)
}

class Demo[F[_]](implicit out: Console[F]) {
  private val toProducer =
    Kleisli.fromFunction[Id, Boolean] { asNew =>
      if (asNew)
        (DemoDsl.Service("new_foo", None): DemoDsl).r[NonEmptyList[PlantUml]]
      else
        (DemoDsl.Service("foo", None): DemoDsl).tag[NonEmptyList[PlantUml]]("foo")
    }

  private val toConsumer =
    Kleisli.fromFunction[Id, Demo.BarAppearance] {
      case Demo.BarAppearance.AsService =>
        (DemoDsl.Service("bar", None): DemoDsl).r[NonEmptyList[PlantUml]]

      case Demo.BarAppearance.AsHydra =>
        (DemoDsl.Service("bar", None): DemoDsl).r[NonEmptyList[PlantUml]]

      case Demo.BarAppearance.WithBuffer =>
        (DemoDsl.Service("bar", None): DemoDsl).r[NonEmptyList[PlantUml]]
    }

  val renderBig =
    NonEmptyList
      .of(
        toProducer.local[Demo.ConfigBasket](_.isNew),
        toConsumer.local[Demo.ConfigBasket](_.barStyle)
      )
      .traverse(_.run)
      .andThen(xs => xs.map(x => x: Renderable[NonEmptyList[PlantUml]]))
      .andThen(xs => Renderable.renderMany[NonEmptyList[PlantUml]](xs))
      .andThen(PlantUml.render[NonEmptyList[PlantUml]])

  def run: F[Unit] =
    out.println(renderBig(Demo.ConfigBasket(isNew = false, Demo.BarAppearance.AsHydra)))
}
