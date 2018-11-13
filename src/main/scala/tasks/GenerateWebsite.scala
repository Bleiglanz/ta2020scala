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
package tasks

import helper.IO
import model.entities.{Document, Meldungen, Steckscheiben}
import slick.jdbc.PostgresProfile
import ta2020.Config


trait ContentMap {
  def meldungen: Seq[Meldungen] = Seq.empty
  def documents: Seq[Document] = Seq.empty
  def blinds: Seq[Steckscheiben] = Seq.empty
  def scopeitem : List[Map[String,String]] = Nil
  def baselink : String = "./"
  def pagination : Int = 20
}


final case class Page(name:String, html:String, contentMap:ContentMap)

final case class PageName(name:String, html:String)

final case object GenerateWebsite extends TaskTrait {

  val info = "generating website"

  override def run(): Unit = {

    val config = Config

    implicit val db: PostgresProfile.api.Database = config.db

    def getMap(pn:PageName):ContentMap = pn.name match {
      case "Scope" =>     new ContentMap { override def meldungen: Seq[Meldungen] = IO.executeDBIOQuery(Meldungen.selectAction)}
      case "Documents" => new ContentMap { override def documents: Seq[Document] = IO.executeDBIOQuery(Document.selectAction)}
      case "Blinds" =>    new ContentMap { override def blinds: Seq[Steckscheiben] = IO.executeDBIOQuery(Steckscheiben.selectAction)}
    }

     List(PageName("Scope", "index"),PageName("Documents", "documents"),PageName("Blinds", "blinds")) foreach { pn =>
      val p:Page = Page(pn.name, pn.html, getMap(pn))
      print(s"..generate page: ${p.html}\n")
      IO.writeUTF8File(Config.outputdir + "" + p.html +".html", html.index(p, config).toString())
    }
  }
}














