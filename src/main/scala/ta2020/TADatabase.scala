package ta2020

import slick.jdbc.SQLiteProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration


object TADatabase  {

  val documents  = TableQuery[Documents]

  val database = Database.forConfig("ta2020.db")

  def create() = {

    try {


      val setup = DBIO.seq(
        // Create the tables, including primary and foreign keys
        documents.schema.create,
        documents += (101, "Acme, Inc.", "99 Market Street")
      )

      val setupFuture = database.run(setup)

      Await.ready(setupFuture,Duration.Inf)
    } finally database.close
  }

}

class Documents(tag: Tag) extends Table[(Int, String, String)](tag, "DOCUMENTS") {
  def id = column[Int]("ID", O.PrimaryKey)
  def name = column[String]("NAME")
  def path = column[String]("PATH")
  def * = (id,name,path)

}