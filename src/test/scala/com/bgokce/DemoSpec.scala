package com.bgokce

import org.scalatest.{FeatureSpec, GivenWhenThen}
import org.scalatest.Matchers._


class DemoSpec extends FeatureSpec with GivenWhenThen{
  info("As a vngrs employers")
  info("I want to learn scala")
  info("So that I can use it for my next project")

  feature("Demo"){
    scenario("Sum of two number") {
      Given("I create demo")
      val d = new Demo
      When("I adding 2+3")
      val total = d.total(2,3)
      Then("Total number should be 5")
      assert(total == 5, s"Total is not 5, it was $total")
      total should equal(5)
    }
  }
}

