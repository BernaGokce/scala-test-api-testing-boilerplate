package com.apitest

import com.softwaremill.sttp._
import com.softwaremill.sttp.circe._
import io.circe.{Json, JsonObject}
import org.scalatest.Matchers._
import org.scalatest.{BeforeAndAfter, FeatureSpec, GivenWhenThen}

class BookStoreAuthorSpec extends FeatureSpec with GivenWhenThen with BeforeAndAfter with DatabaseSupport with HttpSupport {
  info("As bookstore user")
  info("I want to see authors detail")
  info("So that I can have all author information")
//
//  before {
//    truncateTable("books")
//    truncateTable("authors")
//  }
//
//  after {
//    truncateTable("books")
//    truncateTable("authors")
//  }
  var authorId = ""
  var authorName = ""

  feature("Authors Operation") {
    scenario("Create Author") {
      Given("I set create author body")
      truncateTable("books")
      truncateTable("authors")

      val requestPayload =
        Json.obj(
          "name" -> Json.fromString("Akif Tutuncu")
        )

      When("I send create author request")
      val request = sttp
        .post(uri"http://localhost:8085/authors")
        .body(requestPayload)
        .response(asJson[JsonObject])

      val response = request.send()

      Then("Response should be 201")
      response.code should equal(201)

      Then("Create author response body should include id")
      val body = checkAndGetJsonBody(response)
      body("id").isDefined should equal(true)
      authorId = body("id").get.as[String].right.get

      Then("Create author response body should include author name")
      body("name") should equal(Some(Json.fromString("Akif Tutuncu")))
      authorName = body("name").get.as[String].right.get
    }

    scenario("Get All Authors") {
      When("I send get all authors request")
      val request = sttp
        .get(uri"http://localhost:8085/authors")
        .response(asJson[List[Json]])

      val response = request.send()

      Then("Get All authors request should return a list of all authors as a Json array")
      val body = checkAndGetJsonBody(response)
      body.size should equal(1)
    }

    scenario("Get Specific Author") {
      When("I send get specific author request")
      val request = sttp
        .get(uri"http://localhost:8085/authors/$authorId")
        .response(asJson[JsonObject])

      val response = request.send()

      Then("Specific author should be shown")
      val body = checkAndGetJsonBody(response)

      Then("Specific author id should be shown")
      body("id").isDefined should equal(true)

      Then("Specific author name should be shown")
      body("name") should equal(Some(Json.fromString("Akif Tutuncu")))
    }

    scenario("Get Specific Author via name") {
      When("I send get specific author via name request")
      val request = sttp
        .get(uri"http://localhost:8085/authors?name=$authorName")
        .response(asJson[List[Json]])

      val response = request.send()

      Then("Specific author name should return as a list")
      val body = checkAndGetJsonBody(response)
      body.size should equal(1)
    }

    scenario("Change author information") {
      Given("I set new information of authors as payload")
      val requestPayload =
        Json.obj(
          "name" -> Json.fromString("Akif Tutuncu Updated")
        )

      When("I send update author name request")
      val request = sttp
        .put(uri"http://localhost:8085/authors/$authorId")
        .body(requestPayload)
        .response(asJson[JsonObject])

      val response = request.send()

      Then("Updated author name should return correctly")
      val body = checkAndGetJsonBody(response)
      body("name") should equal(Some(Json.fromString("Akif Tutuncu Updated")))

    }

    scenario("Delete Author") {
      When("I send delete request")
      val request = sttp
        .delete(uri"http://localhost:8085/authors/$authorId")
        .response(asJson[JsonObject])

      val response = request.send()

      Then("Response should be 200")
      response.code should equal(200)
    }
  }
}
