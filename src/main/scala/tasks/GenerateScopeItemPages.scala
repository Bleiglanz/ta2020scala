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

import java.sql.Connection

import helper.IO
import model.entities.Meldungen
import slick.jdbc.PostgresProfile
import ta2020.Config

object GenerateScopeItemPages extends TaskTrait {
  private val config = Config
  implicit val db: PostgresProfile.api.Database = config.db

  override def run(): Unit = {
    val conn: Connection = db.source.createConnection()
    IO.executeDBIOQuery(Meldungen.selectAction) foreach { sc=>

      val row = helper.JDBC.selectRowtoColumnMaps(s"""select * from ex_meldungen where turnaround_nr_='${sc.tanr}'""")(conn)

      val doc = helper.JDBC.selectRowtoColumnMaps(s"""select fullpath as dokument from document where tanr ='${sc.tanr}'""")(conn)

      val p:Page = Page("ScopeItem " + sc.tanr,"scope/ta" + sc.tanr,
        new ContentMap {
          override def scopeitem: List[Map[String, String]] = List(row,doc)
          override def baselink: String = "../"
          override def pagination : Int = -1
        })

      IO.writeUTF8File(Config.outputdir + "" + p.html + ".html", html.index(p, config).toString())
      print(p.html)
      print('\n')
    }
    conn.close()
  }
  override val info: String = "generate pages for scope items"
}
