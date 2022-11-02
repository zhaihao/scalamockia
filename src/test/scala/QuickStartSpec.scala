/*
 * Copyright (c) 2020-2022.
 * OOON.ME ALL RIGHTS RESERVED.
 * Licensed under the Mozilla Public License, version 2.0
 * Please visit <http://ooon.me> or mail to zhaihao@ooon.me
 */

package me.ooon.scalamockia

import com.typesafe.scalalogging.StrictLogging
import org.scalamock.scalatest.MockFactory
import test.BaseSpec

/**
  * QuickStartSpec
  *
  * @author zhaihao
  * @version 1.0
  * @since 2022/11/2 13:53
  */
class QuickStartSpec extends BaseSpec with StrictLogging with MockFactory {
  import Greetings.Formatter
  "mock 一个实现" in {
    val mockFormatter = mock[Formatter]
    (mockFormatter.format _).expects("Mr Bond").returning("Ah, Mr Bond. I've been expecting you").anyNumberOfTimes()

    Greetings.sayHi("Mr Bond", mockFormatter)
    Greetings.sayHi("Mr Bond", mockFormatter)
  }

  "mockito style" in {
    val mockFormatter = stub[Formatter]
    val bond          = "Mr Bond"
    (mockFormatter.format _).when(bond).returning("Ah, Mr Bond. I've been expecting you")
    Greetings.sayHi(bond, mockFormatter)
    (mockFormatter.format _).verify(bond).once()
  }

  "带参数 mock" in {
    val australianFormat = mock[Formatter]
    (australianFormat.format _).expects(*).onCall { s: String => s"Good day $s" }.twice()
    Greetings.sayHi("Wendy", australianFormat)
    Greetings.sayHi("Gray", australianFormat)
  }

  "mock 参数校验" in {
    val names         = Set("Nats", "Lucy", "Happy", "Era", "Gray", "Wendy", "Carla")
    val mockFormatter = mock[Formatter]
    (mockFormatter.format _).expects(argAssert { s: String => assert(names.contains(s)) }).onCall { s: String => s"Yo1 $s" }

    (mockFormatter.format _).expects(where { s: String => names contains s }).onCall { s: String => s"Yo2 $s" }.twice()

    Greetings.sayHi("Carla", mockFormatter)
    Greetings.sayHi("Happy", mockFormatter)
    Greetings.sayHi("Lucy", mockFormatter)
  }

  "mock 异常" in {
    val brokerFormatter = mock[Formatter]
    (brokerFormatter.format _).expects(*).throwing(new NullPointerException).anyNumberOfTimes()
    intercept[NullPointerException] {
      Greetings.sayHi("tom", brokerFormatter)
    }
  }

  "mock 调用顺序" in {
    val mockFormatter = mock[Formatter]
    inAnyOrder {
      (mockFormatter.format _).expects("Mr Bond").returns("Ah, Mr Bond. I've been expecting you")
      (mockFormatter.format _).expects("Nats").returns("Not now Nats!").atLeastTwice()
    }

    Greetings.sayHi("Nats", mockFormatter)
    Greetings.sayHi("Nats", mockFormatter)
    Greetings.sayHi("Mr Bond", mockFormatter)
    Greetings.sayHi("Nats", mockFormatter)
  }

}

object Greetings {
  trait Formatter { def format(s: String): String }
  // maybe not impl
  object EnglishFormatter  { def format(s: String): String = s"Hello $s" }
  object GermanFormatter   { def format(s: String): String = s"Hallo $s" }
  object JapaneseFormatter { def format(s: String): String = s"こんにちは $s" }

  def sayHi(name: String, formatter: Formatter) = println(formatter.format(name))
}
