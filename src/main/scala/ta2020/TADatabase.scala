
package ta2020

import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape
import ta2020.TADatabase.User

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object TADatabase {

  case class User(id:Long, username:String)


  // Schema for the "message" table:
  final class UsersTable(tag: Tag) extends Table[User](tag, "users") {

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def username: Rep[String] = column[String]("username")
    def * : ProvenShape[User] = (id,username).mapTo[User]
  }

  // Base query for querying the messages table:
  lazy val users = TableQuery[UsersTable]
}

object DB {

  private val db = Database.forConfig("ta2020.db")

  private def exec[T](program: DBIO[T]): T = Await.result(db.run(program), Duration.Inf)

  def read():Unit = {
    println("\nSelecting all messages:")
    exec(TADatabase.users.result) foreach {
      println
    }
    ()
  }


  def batchinsert(data:Seq[User]):Unit = {
    val conn = db.source.createConnection()

    data.foreach { user=>
      val st = db.source.createConnection().prepareStatement("INSERT INTO users (username) VALUES (?)")
      st.setString(1,user.username)
      st.executeUpdate()
      ()
    }
    conn.close()
  }


}