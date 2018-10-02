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

case object Document{
  val all = TableQuery[DocumentTable]
  val schema = all.schema
  def dropAction: DBIOAction[Unit, NoStream, Effect.Schema] = DBIO.seq(schema.drop )
  def createAction: DBIOAction[Unit, NoStream, Effect.Schema] = DBIO.seq(schema.create)
  def insertAction(data:Seq[Document]):DBIOAction[Unit, NoStream, Effect.Write] = DBIO.seq(all ++= data)
  def selectAction: DBIOAction[Unit, NoStream, Effect.Read] = DBIO.seq(all.result)
}

final case class Document (
  id:Option[Long],
  name:String,
  doctype:String,
  fullpath:String,
  extension:String,
  size:Long
)

final class DocumentTable(tag: Tag) extends Table[Document](tag, "document") {
  def id:Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name:Rep[String] = column[String]("name")
  def doctype:Rep[String] = column[String]("doctype")
  def fullpath:Rep[String] = column[String]("fullpath")
  def extension:Rep[String] = column[String]("extension")
  def size:Rep[Long] = column[Long]("size")
  def * : ProvenShape[Document] = (id.?,name,doctype,fullpath,extension,size) <> ((Document.apply _).tupled,Document.unapply)
}


