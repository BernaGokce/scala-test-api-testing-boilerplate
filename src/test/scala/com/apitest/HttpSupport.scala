package com.apitest

import com.softwaremill.sttp._
import com.softwaremill.sttp.circe._
import io.circe.Error
import io.circe.JsonObject

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
}
