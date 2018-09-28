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

object Mergetable{
  val all = TableQuery[MergetableTable]
  val schema = all.schema
  def dropAction: DBIOAction[Unit, NoStream, Effect.Schema] = DBIO.seq(schema.drop )
  def createAction: DBIOAction[Unit, NoStream, Effect.Schema] = DBIO.seq(schema.create)
  def insertAction(data:Seq[Mergetable]):DBIOAction[Unit, NoStream, Effect.Write] = DBIO.seq(all ++= data)
}

case class Mergetable (
  id:Option[Long],
  s1:String,
  s2:String,
  s3:String,
  s4:String,
  s5:String,
  s6:String,
  s7:String,
  s8:String,
  s9:String,
  s10:String,
  s11:String,
  s12:String,
  s13:String,
  s14:String,
  s15:String,
  s16:String,
  s17:String,
  s18:String,
  s19:String,
  s20:String
)

final class MergetableTable(tag: Tag) extends Table[Mergetable](tag, "mergetable") {
  def id:Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def s1:Rep[String] = column[String]("s1")
  def s2:Rep[String] = column[String]("s2")
  def s3:Rep[String] = column[String]("s3")
  def s4:Rep[String] = column[String]("s4")
  def s5:Rep[String] = column[String]("s5")
  def s6:Rep[String] = column[String]("s6")
  def s7:Rep[String] = column[String]("s7")
  def s8:Rep[String] = column[String]("s8")
  def s9:Rep[String] = column[String]("s9")
  def s10:Rep[String] = column[String]("s10")
  def s11:Rep[String] = column[String]("s11")
  def s12:Rep[String] = column[String]("s12")
  def s13:Rep[String] = column[String]("s13")
  def s14:Rep[String] = column[String]("s14")
  def s15:Rep[String] = column[String]("s15")
  def s16:Rep[String] = column[String]("s16")
  def s17:Rep[String] = column[String]("s17")
  def s18:Rep[String] = column[String]("s18")
  def s19:Rep[String] = column[String]("s19")
  def s20:Rep[String] = column[String]("s20")
  def * : ProvenShape[Mergetable] = (id.?,s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s11,s12,s13,s14,s15,s16,s17,s18,s19,s20) <> ((Mergetable.apply _).tupled,Mergetable.unapply)
}


