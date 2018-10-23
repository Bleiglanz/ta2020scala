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
package model.entities

import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape
import slick.sql.SqlProfile.ColumnOption.SqlType
import java.sql.Timestamp

case object Meldungen{
  val all = TableQuery[MeldungenTable]
  val schema = all.schema
  def dropAction: DBIOAction[Unit, NoStream, Effect.Schema] = DBIO.seq(schema.drop )
  def createAction: DBIOAction[Unit, NoStream, Effect.Schema] = DBIO.seq(schema.create)
  def insertAction(data:Seq[Meldungen]):DBIOAction[Unit, NoStream, Effect.Write] = DBIO.seq(all ++= data)
  def selectAction: DBIO[Seq[Meldungen]] = all.result
}

final case class Meldungen (
  id:Option[Long],
  tanr:String,
  shorttext:String,
  longtext:String,
  created: Timestamp,
  updated: Timestamp
)


final class MeldungenTable(tag: Tag) extends Table[Meldungen](tag, "meldungen") {
  def id:Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def tanr:Rep[String] = column[String]("tanr")
  def shorttext:Rep[String] = column[String]("shorttext")
  def longtext:Rep[String] = column[String]("longtext")
  def created:Rep[Timestamp] = column[Timestamp]("created", SqlType("timestamp not null default CURRENT_TIMESTAMP"))
  def updated:Rep[Timestamp] = column[Timestamp]("updated", SqlType("timestamp not null default CURRENT_TIMESTAMP"))
  def * : ProvenShape[Meldungen] = (id.?,tanr,shorttext,longtext,created,updated) <> ((Meldungen.apply _).tupled,Meldungen.unapply)
}


