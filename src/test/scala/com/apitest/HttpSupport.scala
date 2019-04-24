package com.apitest

import com.softwaremill.sttp._
import com.softwaremill.sttp.circe._
import io.circe.Error
import io.circe.JsonObject
import org.scalatest.Matchers.equal
import org.scalatest.Matchers._

trait HttpSupport {
  implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()

  def checkAndGetJsonBody[T](response: Response[Either[DeserializationError[Error], T]]): T = {
    val maybeBody =
      for {
        successfulBody <- response.body
        parsedBody     <- successfulBody
      } yield parsedBody

    assert(maybeBody.isRight, "http request failed")

    maybeBody.right.get
  }

  def expandPath(uri: Uri, paths: String*) : Uri = uri.copy(path = uri.path ++ paths)

  def checkResponseCode(response: Response[_], code: Int): Unit ={
    response.code should equal(code)
  }

}
