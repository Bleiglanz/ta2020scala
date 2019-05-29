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

case object Tikadocument{
  val all = TableQuery[TikadocumentTable]
  val schema = all.schema
  def dropAction: DBIOAction[Unit, NoStream, Effect.Schema] = DBIO.seq(schema.drop )
  def createAction: DBIOAction[Unit, NoStream, Effect.Schema] = DBIO.seq(schema.create)
  def insertAction(data:Seq[Tikadocument]):DBIOAction[Unit, NoStream, Effect.Write] = DBIO.seq(all ++= data)
  def selectAction: DBIO[Seq[Tikadocument]] = all.result
}

final case class Tikadocument (
  id:Option[Long],
  name:String,
  doctype:String,
  fullpath:String,
  extension:String,
  file_last_modified:Timestamp,
  tikaparse:String,
  created: Timestamp,
  updated: Timestamp
)


final class TikadocumentTable(tag: Tag) extends Table[Tikadocument](tag, "tikadocument") {
  def id:Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name:Rep[String] = column[String]("name")
  def doctype:Rep[String] = column[String]("doctype")
  def fullpath:Rep[String] = column[String]("fullpath")
  def extension:Rep[String] = column[String]("extension")
  def file_last_modified:Rep[Timestamp] = column[Timestamp]("file_last_modified")
  def tikaparse:Rep[String] = column[String]("tikaparse")
  def created:Rep[Timestamp] = column[Timestamp]("created", SqlType("timestamp not null default CURRENT_TIMESTAMP"))
  def updated:Rep[Timestamp] = column[Timestamp]("updated", SqlType("timestamp not null default CURRENT_TIMESTAMP"))
  def * : ProvenShape[Tikadocument] = (id.?,name,doctype,fullpath,extension,file_last_modified,tikaparse,created,updated) <> ((Tikadocument.apply _).tupled,Tikadocument.unapply)
}


