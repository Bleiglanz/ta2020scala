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

case object Steckscheiben{
  val all = TableQuery[SteckscheibenTable]
  val schema = all.schema
  def dropAction: DBIOAction[Unit, NoStream, Effect.Schema] = DBIO.seq(schema.drop )
  def createAction: DBIOAction[Unit, NoStream, Effect.Schema] = DBIO.seq(schema.create)
  def insertAction(data:Seq[Steckscheiben]):DBIOAction[Unit, NoStream, Effect.Write] = DBIO.seq(all ++= data)
  def selectAction: DBIO[Seq[Steckscheiben]] = all.result
}

final case class Steckscheiben (
  id:Option[Long],
  sysnr:String,
  sysnr2:String,
  grnr:String,
  lfdnr:String,
  beschreibung:String,
  rkl:String,
  dn:String,
  pn:String,
  pid:String,
  apparatenr:String,
  brille:Boolean,
  job:Boolean,
  syssteck:Boolean,
  created: Timestamp,
  updated: Timestamp
)


final class SteckscheibenTable(tag: Tag) extends Table[Steckscheiben](tag, "steckscheiben") {
  def id:Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def sysnr:Rep[String] = column[String]("sysnr")
  def sysnr2:Rep[String] = column[String]("sysnr2")
  def grnr:Rep[String] = column[String]("grnr")
  def lfdnr:Rep[String] = column[String]("lfdnr")
  def beschreibung:Rep[String] = column[String]("beschreibung")
  def rkl:Rep[String] = column[String]("rkl")
  def dn:Rep[String] = column[String]("dn")
  def pn:Rep[String] = column[String]("pn")
  def pid:Rep[String] = column[String]("pid")
  def apparatenr:Rep[String] = column[String]("apparatenr")
  def brille:Rep[Boolean] = column[Boolean]("brille")
  def job:Rep[Boolean] = column[Boolean]("job")
  def syssteck:Rep[Boolean] = column[Boolean]("syssteck")
  def created:Rep[Timestamp] = column[Timestamp]("created", SqlType("timestamp not null default CURRENT_TIMESTAMP"))
  def updated:Rep[Timestamp] = column[Timestamp]("updated", SqlType("timestamp not null default CURRENT_TIMESTAMP"))
  def * : ProvenShape[Steckscheiben] = (id.?,sysnr,sysnr2,grnr,lfdnr,beschreibung,rkl,dn,pn,pid,apparatenr,brille,job,syssteck,created,updated) <> ((Steckscheiben.apply _).tupled,Steckscheiben.unapply)
}


