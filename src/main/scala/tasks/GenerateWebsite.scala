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
import model.entities.{Document, Meldungen}
import slick.jdbc.PostgresProfile
import ta2020.Config

case object GenerateWebsite extends TaskTrait {

  trait ContentMap {
    def meldungen:Seq[Meldungen]=Seq.empty
    def documents:Seq[Document]=Seq.empty
  }

  val info = "generating website"

  override def run(): Unit = {

    val config = Config

    implicit val db: PostgresProfile.api.Database = config.db



    val navi:List[Navigation]= List(Navigation("Scope","./index.html"), Navigation("Dokumente","./documents.html"))

    val meldungenMap = new ContentMap {override def meldungen: Seq[Meldungen] = IO.executeDBIOQuery(Meldungen.selectAction)}
    IO.writeUTF8File(
      Config.outputdir + "index.html",
      html.index(meldungenMap, "Scope", navi, config).toString()
    )

    val documentsMap = new ContentMap { override def documents: Seq[Document] = IO.executeDBIOQuery(Document.selectAction)}
    IO.writeUTF8File(
      Config.outputdir + "documents.html",
      html.index(documentsMap, "Dokumente", navi, config).toString()
    )

  }
}

sealed trait NavLink {
  val name:String
  val url:String
}
final case class Navigation(name:String, url:String) extends NavLink
