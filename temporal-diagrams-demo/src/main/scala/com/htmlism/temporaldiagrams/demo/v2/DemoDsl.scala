package com.htmlism.temporaldiagrams.demo.v2

import cats.data.NonEmptyList

import com.htmlism.temporaldiagrams.plantuml.PlantUml
import com.htmlism.temporaldiagrams.v2.BrightEncoder

sealed trait DemoDsl

object DemoDsl {
  case class ClusterService(name: String, dependency: Option[String], asCluster: Boolean) extends DemoDsl
  case class Buffered(name: String, dependency: Option[String])                           extends DemoDsl

  implicit val demoBrightEncoder: BrightEncoder[NonEmptyList[PlantUml], DemoDsl] =
    new BrightEncoder[NonEmptyList[PlantUml], DemoDsl] {
      def encodeBrightly(x: DemoDsl, isBright: Boolean): NonEmptyList[PlantUml] = {
        x match {
          case ClusterService(n, oDep, asCluster) =>
            if (asCluster)
              NonEmptyList
                .of(1, 2, 3, 4)
                .flatMap { i =>
                  NonEmptyList
                    .of[PlantUml](
                      PlantUml.Component(n + i.toString, None, Option.when(isBright)("Service")),
                      skin(isBright)
                    )
                    .applySome(oDep) { (a, d) =>
                      a.appendList(List(PlantUml.Arrow(d + i.toString, n)))
                    }
                }
            else
              NonEmptyList
                .of[PlantUml](
                  PlantUml.Component(n, None, Option.when(isBright)("Service")),
                  skin(isBright)
                )
                .applySome(oDep) { (a, d) =>
                  a.appendList(List(PlantUml.Arrow(d, n)))
                }

          case Buffered(n, oDep) =>
            NonEmptyList
              .of[PlantUml](
                PlantUml.Component(n, None, Option.when(isBright)("Service")),
                skin(isBright),
                PlantUml.Queue(n + "_queue", None, None),
                PlantUml.Arrow(n + "_queue", n),
                queueSkin
              )
              .applySome(oDep) { (a, d) =>
                a.appendList(List(PlantUml.Arrow(d, n + "_queue")))
              }
        }
      }
    }

  private val queueSkin =
    PlantUml
      .SkinParamGroup("queue")
      .and("fontStyle", "bold")
      .and("fontColor", "#AAA")
      .and("backgroundColor", "white")
      .and("borderColor", "#AAA")
      .and("borderThickness", "2")

  private def skin(isBright: Boolean) =
    if (isBright)
      PlantUml
        .SkinParamGroup("component", "Service")
        .and("fontStyle", "bold")
        .and("fontColor", "white")
        .and("backgroundColor", "#586ba4")
        .and("borderColor", "#223336")
        .and("borderThickness", "2")
    else
      PlantUml
        .SkinParamGroup("component")
        .and("fontStyle", "bold")
        .and("fontColor", "#AAA")
        .and("backgroundColor", "white")
        .and("borderColor", "#AAA")
        .and("borderThickness", "2")

  case class ConfigBasket(fooStyle: ConfigBasket.ServiceAppearance, barStyle: ConfigBasket.ServiceAppearance)

  object ConfigBasket {
    sealed trait ServiceAppearance

    object ServiceAppearance {
      case object AsSingleton extends ServiceAppearance

      case object AsCluster extends ServiceAppearance

      case object WithBuffer extends ServiceAppearance
    }
  }
}
