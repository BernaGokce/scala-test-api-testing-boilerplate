package com.apitest

import java.net.URI

import com.softwaremill.sttp.Uri
import io.circe.parser.parse

import scala.io.Source

trait JsonConfigSupport {
  def getUrl(service: String, environment: String, path: String): Uri = {
    val host: String = getKey(environmentMap, service, environment).getOrElse(throw new Exception(s"Cannot find service $service in $environment"))
    val urlPath: String = getKey(urlsMap, service, path).getOrElse(throw new Exception(s"Cannot find path $path in service $service"))
    Uri(new URI(s"$host/$urlPath"))
  }

  private val environmentMap: Map[String, Map[String, String]] = parseConfig("src/test/resources/env.json")
  private val urlsMap: Map[String, Map[String, String]]        = parseConfig("src/test/resources/urls.json")

  private def parseConfig(file: String): Map[String, Map[String, String]] = {
    val source = Source.fromFile(file).getLines.mkString

    val maybeConfig =
      for {
        json   <- parse(source)
        parsed <- json.as[Map[String, Map[String, String]]]
      } yield parsed

    maybeConfig.getOrElse(throw new Exception(s"Cannot parse $file"))
  }

  private def getKey(map: Map[String, Map[String, String]], service: String, key: String): Option[String] =
    for {
      config <- map.get(service)
      value  <- config.get(key)
    } yield value
}
