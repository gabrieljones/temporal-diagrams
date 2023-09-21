package com.htmlism.temporaldiagrams.demo.v2

import scala.util.chaining._

import cats._
import cats.data.Kleisli
import cats.data.NonEmptyList
import cats.effect._
import cats.syntax.all._

import com.htmlism.temporaldiagrams.demo.FilePrinterAlg
import com.htmlism.temporaldiagrams.plantuml.PlantUml
import com.htmlism.temporaldiagrams.v2.Renderable
import com.htmlism.temporaldiagrams.v2.syntax._

object Demo extends Demo[IO](FilePrinterAlg[IO]) with IOApp.Simple {
  sealed trait BarAppearance

  object BarAppearance {
    case object AsService extends BarAppearance

    case object AsHydra extends BarAppearance

    case object WithBuffer extends BarAppearance
  }

  case class ConfigBasket(isNew: Boolean, barStyle: BarAppearance)
}

class Demo[F[_]: Applicative](out: FilePrinterAlg[F]) {
  private val toProducer =
    Kleisli.fromFunction[Id, Boolean][Renderable[NonEmptyList[PlantUml]]] { asNew =>
      if (asNew)
        DemoDsl.ClusterService("new_foo", None, asHydra = false)
      else
        DemoDsl.ClusterService("foo", None, asHydra = false).tag("foo")
    }

  private val toConsumer =
    Kleisli.fromFunction[Id, Demo.BarAppearance][Renderable[NonEmptyList[PlantUml]]] {
      case Demo.BarAppearance.AsService =>
        DemoDsl.ClusterService("bar", "foo".some, asHydra = false).tag("bar")

      case Demo.BarAppearance.AsHydra =>
        DemoDsl.ClusterService("bar", "foo".some, asHydra = true).tag("bar")

      case Demo.BarAppearance.WithBuffer =>
        DemoDsl.Buffered("bar", "new_foo".some)
    }

  val stackGivenCfg =
    NonEmptyList
      .of(
        toProducer.local[Demo.ConfigBasket](_.isNew),
        toConsumer.local[Demo.ConfigBasket](_.barStyle)
      )
      .traverse(_.run)

  val z =
    Demo.ConfigBasket(isNew = false, Demo.BarAppearance.AsService)

  val episodesDeltas =
    List[Demo.ConfigBasket => Demo.ConfigBasket](
      _.copy(isNew = true, barStyle = Demo.BarAppearance.AsHydra),
      _.copy(isNew = true, barStyle = Demo.BarAppearance.WithBuffer)
    )
      .mapAccumulate(z)((s, f) => f(s) -> f(s))
      ._2

  def run: F[Unit] = {
    val cfgs =
      z :: episodesDeltas

    cfgs
      .zipWithIndex
      .traverse { case (cfg, n) =>
        val renders: NonEmptyList[Renderable[NonEmptyList[PlantUml]]] =
          stackGivenCfg(cfg)
            .map(_.extract)

        printNormalDiagram(renders, n) *> printHighlightDiagrams(renders, n)
      }
      .void
  }

  private def printNormalDiagram(renders: NonEmptyList[Renderable[NonEmptyList[PlantUml]]], n: Int) = {
    val str =
      renders
        .pipe(Renderable.renderMany[NonEmptyList[PlantUml]])
        .pipe(_.distinct.sorted)
        .pipe(PlantUml.render(_))
        .mkString_("\n")

    out.print(s"v2-$n.puml")(str)
  }

  private def printHighlightDiagrams(renders: NonEmptyList[Renderable[NonEmptyList[PlantUml]]], n: Int) = {
    val tags =
      Renderable
        .allTags(renders)
        .toList

    tags.traverse_ { t =>
      val str =
        renders
          .pipe(Renderable.renderManyWithTag[NonEmptyList[PlantUml]](_, t))
          .pipe(_.distinct.sorted)
          .pipe(PlantUml.render(_))
          .mkString_("\n")

      out.print(s"v2-$n-$t.puml")(str)
    }
  }
}
