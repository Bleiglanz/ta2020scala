
package ta2020

import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object TADatabase {

  case class User(id:Long, username:String)


  // Schema for the "message" table:
  final class MessageTable(tag: Tag) extends Table[User](tag, "user") {

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def username: Rep[String] = column[String]("content")
    def * : ProvenShape[User] = (id,username).mapTo[User]
  }

  // Base query for querying the messages table:
  lazy val messages = TableQuery[MessageTable]

  // Create an in-memory H2 database;
  val db = Database.forConfig("chapter01")

  // Helper method for running a query in this example file:
  def exec[T](program: DBIO[T]): T = Await.result(db.run(program), Duration.Inf)

  // Run the test query and print the results:
  println("\nSelecting all messages:")
  exec( messages.result ) foreach { println }

}