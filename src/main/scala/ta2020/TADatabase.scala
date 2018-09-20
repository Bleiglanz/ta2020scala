// Copyright (C) 2011-2012 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package ta2020

import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape
import ta2020.TADatabase.User

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object TADatabase {

  final case class User(id:Long, username:String)


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