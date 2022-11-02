/*
 * Copyright (c) 2020-2022.
 * OOON.ME ALL RIGHTS RESERVED.
 * Licensed under the Mozilla Public License, version 2.0
 * Please visit <http://ooon.me> or mail to zhaihao@ooon.me
 */

package me.ooon.scalamockia

import org.scalamock.matchers.ArgCapture.{CaptureAll, CaptureOne}
import org.scalamock.scalatest.MockFactory
import test.BaseSpec

/**
  * MockFeaturesSpec
  *
  * @author zhaihao
  * @version 1.0
  * @since 2022/11/2 14:45
  */
class MockFeaturesSpec extends BaseSpec with MockFactory {
  "mock function" in {
    val m = mockFunction[String, String, Int]
    m.expects("1", "2").returning(3)
    m("1", "2") ==> 3
  }

  "mock object" in {
    val mockHeater = mock[Heater]
    (mockHeater.a _).expects().returning(1)
    mockHeater.a ==> 1
  }

  trait Heater { def a: Int }

  "Polymorphic methods" in {
    trait Foo { def hi[T](x: List[T]): String }
    val m = mock[Foo]
    (m.hi(_: List[Int])).expects(List(1, 2, 3)).anyNumberOfTimes()
    (m.hi[Int] _).expects(List(1, 2, 3)).anyNumberOfTimes()
    (m.hi: List[Int] => String).expects(List(1, 2, 3)).anyNumberOfTimes()
  }

  "Curried methods" in {
    trait Foo { def curried(x: Int)(y: Double): String }
    val fooMock = mock[Foo]
    (fooMock.curried(_: Int)(_: Double)).expects(10, 1.23).anyNumberOfTimes()
  }

  "Methods with implicit parameters" in {
    class Codec()
    trait Memcached { def get(key: String)(implicit codec: Codec): Option[Int] }
    val memcachedMock  = mock[Memcached]
    implicit val codec = new Codec
    (memcachedMock.get(_: String)(_: Codec)).expects("some_key", *).returning(Some(123))
    memcachedMock.get("some_key") ==> Some(123)
  }

  "Repeated parameters" in {
    trait Foo { def takesRepeatedParameter(x: Int, ys: String*): Unit }
    val m = mock[Foo]
    (m.takesRepeatedParameter _).expects(42, Seq("1", "2")).anyNumberOfTimes()
  }

  "on Call" in {
    trait Foo { def inc(a: Int): Int }
    val m = mock[Foo]
    (m.inc _)
      .expects(*)
      .onCall { a: Int =>
        if (a == 0) throw new RuntimeException("error")
        else a + 1
      }
      .twice()

    m.inc(2) ==> 3
    intercept[RuntimeException] { m.inc(0) }
  }

  "参数捕捉" in {
    val m1 = mockFunction[Int, Int]
    val c1 = CaptureOne[Int]()
    m1.expects(capture(c1)).once()
    m1(42)
    c1.value ==> 42

    val m2 = mockFunction[Int, Int]
    val c2 = CaptureAll[Int]()
    m2.expects(capture(c2)).repeat(3)
    m2(1)
    m2(2)
    m2(3)
    c2.value  ==> 3
    c2.values ==> Seq(1, 2, 3)
  }
}
