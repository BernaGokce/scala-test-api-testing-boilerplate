package com.apitest

import java.util.UUID

import cats.effect.{ContextShift, IO}
import doobie.implicits._
import doobie.util.fragment.Fragment
import doobie.util.transactor.Transactor

import scala.concurrent.ExecutionContext

trait DatabaseSupport {
  implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  val db: Transactor[IO] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/bookstore",
    "bookstore",
    "bookstore"
  )

  def truncateTable(table: String): Unit = {
    val io = for {
      _ <- Fragment.const(s"TRUNCATE TABLE $table CASCADE").update.run
    } yield {
      println(s"Truncated table $table")
    }

    io.transact[IO](db).unsafeRunSync()
  }

  def addAuthor(id: UUID, name: String): Unit = {
    val io = for {
      _ <- sql"INSERT INTO authors (id, name) VALUES (${id.toString}::uuid, $name)".update.run
    } yield {
      println(s"$name is added on db")
    }
    io.transact[IO](db).unsafeRunSync()
  }
}
