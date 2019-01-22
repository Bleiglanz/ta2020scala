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

case object Excelsheet{
  val all = TableQuery[ExcelsheetTable]
  val schema = all.schema
  def dropAction: DBIOAction[Unit, NoStream, Effect.Schema] = DBIO.seq(schema.drop )
  def createAction: DBIOAction[Unit, NoStream, Effect.Schema] = DBIO.seq(schema.create)
  def insertAction(data:Seq[Excelsheet]):DBIOAction[Unit, NoStream, Effect.Write] = DBIO.seq(all ++= data)
  def selectAction: DBIO[Seq[Excelsheet]] = all.result
}

final case class Excelsheet (
  id:Option[Long],
  filename:String,
  sheetname:String,
  tablename:String,
  cols:Int,
  rows:Int,
  file_last_modified:Timestamp,
  created: Timestamp,
  updated: Timestamp
)


final class ExcelsheetTable(tag: Tag) extends Table[Excelsheet](tag, "excelsheet") {
  def id:Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def filename:Rep[String] = column[String]("filename")
  def sheetname:Rep[String] = column[String]("sheetname")
  def tablename:Rep[String] = column[String]("tablename")
  def cols:Rep[Int] = column[Int]("cols")
  def rows:Rep[Int] = column[Int]("rows")
  def file_last_modified:Rep[Timestamp] = column[Timestamp]("file_last_modified")
  def created:Rep[Timestamp] = column[Timestamp]("created", SqlType("timestamp not null default CURRENT_TIMESTAMP"))
  def updated:Rep[Timestamp] = column[Timestamp]("updated", SqlType("timestamp not null default CURRENT_TIMESTAMP"))
  def * : ProvenShape[Excelsheet] = (id.?,filename,sheetname,tablename,cols,rows,file_last_modified,created,updated) <> ((Excelsheet.apply _).tupled,Excelsheet.unapply)
}


