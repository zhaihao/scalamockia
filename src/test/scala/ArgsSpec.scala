/*
 * Copyright (c) 2020-2022.
 * OOON.ME ALL RIGHTS RESERVED.
 * Licensed under the Mozilla Public License, version 2.0
 * Please visit <http://ooon.me> or mail to zhaihao@ooon.me
 */

package me.ooon.scalamockia

import org.scalamock.scalatest.MockFactory
import test.BaseSpec

/**
  * ArgsSpec
  *
  * @author zhaihao
  * @version 1.0
  * @since 2022/11/2 15:01
  */
class ArgsSpec extends BaseSpec with MockFactory {

  "function args" in {
    val m2 = mockFunction[String, Any, Unit]
    m2.expects("foo", *).returning(()).repeat(3)
    m2("foo", 42)
    m2("foo", 1.0)
    m2("foo", null)
  }

  "epsilon" in {
    val m = mockFunction[Float, Float]
    m.expects(~42.0).repeat(4)
    m(42)
    m(42.0f)
    m(42.00001f)
    m(41.999f)
  }
  "where" in {
    val m = mockFunction[Int, Int, Int]
    m.expects(where { _ < _ }).twice()
    m(1, 2)
  }
}
