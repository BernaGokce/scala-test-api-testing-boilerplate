package com.apitest

import java.util.UUID

import com.softwaremill.sttp._
import com.softwaremill.sttp.circe._
import io.circe.{Json, JsonObject}
import org.scalatest.Matchers._
import org.scalatest.{BeforeAndAfter, FeatureSpec, GivenWhenThen}

class BookStoreBookSpec extends FeatureSpec with GivenWhenThen with BeforeAndAfter with DatabaseSupport with HttpSupport {
  info("As bookstore user")
  info("I want to see books detail")
  info("So that I can have all book information")

  val authorId = UUID.randomUUID()
  var bookName = ""
  var bookAuthorName = ""
  var bookId = ""

  feature("Books Operation") {
    scenario("Create books for specific author") {

      Given("I set create book body")
      truncateTable("books")
      truncateTable("authors")
      addAuthor(authorId,"Berna Gokce")

      val requestPayload =
        Json.obj(
          "isbn" -> Json.fromString("1234"),
          "name" -> Json.fromString("CONSULTANCY"),
          "price" -> Json.fromInt(300)
        )

      When("I send create author request")
      val request = sttp
        .post(uri"http://localhost:8085/authors/$authorId/books")
        .body(requestPayload)
        .response(asJson[JsonObject])

      val response = request.send()

      Then("Response should be 201")
      response.code should equal(201)

      Then("Create book response body should include id")
      val body = checkAndGetJsonBody(response)
      body("id").isDefined should equal(true)
      bookId = body("id").get.as[String].right.get

      Then("Create book response body should include book name")
      body("name") should equal(Some(Json.fromString("CONSULTANCY")))
      bookName = body("name").get.as[String].right.get

      Then("Create book response body should include author name")
      body("author") should equal(Some(Json.fromString("Berna Gokce")))
      bookAuthorName = body("author").get.as[String].right.get

      Then("Create book response body should include book price")
      body("price") should equal(Some(Json.fromInt(300)))

      Then("Create book response body should include book isbn")
      body("isbn") should equal(Some(Json.fromString("1234")))
    }

    scenario("Get author specific books") {
      When("I send get books for specific author endpoint")
      val request = sttp
        .get(uri"http://localhost:8085/authors/$authorId/books/$bookId")
        .response(asJson[JsonObject])

      val response = request.send()

      Then("Create book response body should include id")
      val body = checkAndGetJsonBody(response)
      body("id").isDefined should equal(true)

      Then("Create book response body should include book name")
      body("name") should equal(Some(Json.fromString("CONSULTANCY")))

      Then("Create book response body should include author name")
      body("author") should equal(Some(Json.fromString("Berna Gokce")))

      Then("Create book response body should include book price")
      body("price") should equal(Some(Json.fromInt(300)))

      Then("Create book response body should include book isbn")
      body("isbn") should equal(Some(Json.fromString("1234")))
    }

    scenario("Get all books") {
      When("I send get all books request")
      val request = sttp
        .get(uri"http://localhost:8085/books")
        .response(asJson[List[Json]])
      val response = request.send()

      Then("Get all books request should return a list of book")
      val body = checkAndGetJsonBody(response)
      body.size should equal(1)
    }

    scenario("Get books via its name") {
      When("I send get books via its name")
      val request = sttp
        .get(uri"http://localhost:8085/books?name=$bookName")
        .response(asJson[List[Json]])
      val response = request.send()

      Then("Get books via its name should return book list")
      val body = checkAndGetJsonBody(response)
      body.size should equal(1)
    }

    scenario("Change book information") {
      Given("I set change book information")
      val requestPayload =
        Json.obj(
          "isbn" -> Json.fromString("123456"),
          "name" -> Json.fromString("CONSULTANCY UPDATED"),
          "price" -> Json.fromInt(400)
        )
      When("I send change book information")
      val request = sttp
        .put(uri"http://localhost:8085/authors/$authorId/books/$bookId")
        .body(requestPayload)
        .response(asJson[JsonObject])

      val response = request.send()

      Then("Create book response body should include id")
      val body = checkAndGetJsonBody(response)
      body("id").isDefined should equal(true)

      Then("Create book response body should include book name")
      body("name") should equal(Some(Json.fromString("CONSULTANCY UPDATED")))

      Then("Create book response body should include author name")
      body("author") should equal(Some(Json.fromString("Berna Gokce")))
      bookAuthorName = body("author").get.as[String].right.get

      Then("Create book response body should include book price")
      body("price") should equal(Some(Json.fromInt(400)))

      Then("Create book response body should include book isbn")
      body("isbn") should equal(Some(Json.fromString("123456")))
    }

    scenario("Author should not be deleted if he/she has book"){
      When("I send delete request for author who has book")
      val request = sttp
        .delete(uri"http://localhost:8085/authors/$authorId")
        .response(asJson[JsonObject])

      val response = request.send()

      Then("Response should be 500")
      response.code should equal(500)
    }

    scenario("Delete book") {
      When("I send delete request to book")
      val request = sttp
        .delete(uri"http://localhost:8085/authors/$authorId/books/$bookId")
        .response(asJson[JsonObject])

      val response = request.send()

      Then("Response should be 200")
      response.code should equal(200)

    }
  }


}
