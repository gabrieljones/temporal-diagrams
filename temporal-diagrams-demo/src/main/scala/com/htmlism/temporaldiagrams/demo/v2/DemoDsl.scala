package com.htmlism.temporaldiagrams.demo.v2

import cats.data.NonEmptyList
import cats.syntax.all._

import com.htmlism.temporaldiagrams.plantuml.PlantUml
import com.htmlism.temporaldiagrams.v2.BrightEncoder

sealed trait DemoDsl

object DemoDsl {
  case class Service(name: String, dependency: Option[String])  extends DemoDsl
  case class Hydra(name: String, dependency: Option[String])    extends DemoDsl
  case class Buffered(name: String, dependency: Option[String]) extends DemoDsl

  implicit val demoBrightEncoder: BrightEncoder[NonEmptyList[PlantUml], DemoDsl] =
    new BrightEncoder[NonEmptyList[PlantUml], DemoDsl] {
      def encodeBrightly(x: DemoDsl, isBright: Boolean): NonEmptyList[PlantUml] = {
        x match {
          case Service(n, oDep) =>
            NonEmptyList
              .of[PlantUml](
                PlantUml.Component(n, None, Option.when(isBright)("Service")),
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
              )
              .applySome(oDep) { (a, d) =>
                a.appendList(List(PlantUml.Arrow(d, n)))
              }

          case Hydra(n, oDep) =>
            NonEmptyList
              .of(1, 2, 3)
              .flatMap { i =>
                val name =
                  n + i.toString

                NonEmptyList
                  .one[PlantUml](PlantUml.Component(name, None, "Service".some))
                  .applySome(oDep) { (a, d) =>
                    a.appendList(List(PlantUml.Arrow(d, name)))
                  }
              }

          case Buffered(n, oDep) =>
            NonEmptyList
              .one[PlantUml](PlantUml.Component(n, None, "Service".some))
              .applySome(oDep) { (a, d) =>
                a.appendList(List(PlantUml.Arrow(d, n)))
              }
        }
      }
    }
}
