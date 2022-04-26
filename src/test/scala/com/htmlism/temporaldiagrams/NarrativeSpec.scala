package com.htmlism.temporaldiagrams

import cats.data._
import org.scalatest.Inside
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should._

import com.htmlism.temporaldiagrams.syntax._

class NarrativeSpec extends AnyFlatSpec with Inside with Matchers with NonEmptyListAggregating {
  it should "support progressive building by prepending" in {
    val services =
      NonEmptyList
        .of(Service("foo", None), Service("bar", None))
        .map(_.r.f[Int])

    val narrative =
      services
        .start
        .next("foo" -> 1)
        .next("foo" -> 2)

    narrative.episodeSelectors should contain theSameElementsAs List(
      Nil,
      List("foo" -> 1),
      List("foo" -> 2, "foo" -> 1)
    )
  }

  it should "support resetting" in {
    val services =
      NonEmptyList
        .of(Service("foo", None), Service("bar", None))
        .map(_.r.f[Int])

    val narrative =
      services
        .start
        .next("foo" -> 1)
        .reset("foo" -> 2)

    narrative.episodeSelectors should contain theSameElementsAs List(
      Nil,
      List("foo" -> 1),
      List("foo" -> 2)
    )
  }

  it should "support mass frame selection" in {
    val fooVariants =
      FacetedFrame
        .from("foo", "first" -> Service("foo", None).r.list, "second" -> Service("newfoo", None).r.list)

    val barVariants =
      FacetedFrame
        .from("bar", "first" -> Service("bar", None).r.list, "second" -> Service("newbar", None).r.list)

    val narrative =
      NonEmptyList
        .of(fooVariants, barVariants)
        .start
        .next("foo" -> "second")
        .next("bar" -> "second")

    val episodes =
      narrative.episodes.toList

    episodes(0) should contain theSameElementsAs List(Service("foo", None).r, Service("bar", None).r)
    episodes(1) should contain theSameElementsAs List(Service("newfoo", None).r, Service("bar", None).r)
    episodes(2) should contain theSameElementsAs List(Service("newfoo", None).r, Service("newbar", None).r)
  }
}
