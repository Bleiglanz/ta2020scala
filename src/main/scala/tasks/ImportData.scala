// Copyright (C) 2011-2012 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

// http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package tasks
import helper.IO
import ta2020.{Config, ExcelImport}
import model.entities.{Document, Excelsheet, Meldungen, Steckscheiben}
import slick.jdbc.PostgresProfile
import java.sql.Timestamp
import java.time.Instant

case object ImportData extends TaskTrait {

  val info = "importing data to local database from excel sheets"

  override def run(): Unit = {

    val config = Config
    val now:Timestamp = Timestamp.from(Instant.now)

    implicit val db: PostgresProfile.api.Database = config.db

    IO.executeDBIOSeq(Document.dropAction andThen Document.createAction)
    IO.executeDBIOSeq(Excelsheet.dropAction andThen Excelsheet.createAction)
    IO.executeDBIOSeq(Meldungen.dropAction andThen Meldungen.createAction)
    IO.executeDBIOSeq(Steckscheiben.dropAction andThen Steckscheiben.createAction)

    val docs = IO.getListOfAllowedFiles(config.scandirs, config.scanfiles)
    IO.executeDBIOSeq(Document.insertAction(docs))

    val session = db.createSession
    try {
      val sheets = config.excel2db.map {
        ex:ExcelImport =>
          val (cols,rows) = ta2020.TableFromExcel.procSingleExcelGeneral("ex_", ex.src, ex.sheet, ex.dest, session.conn)
          model.entities.Excelsheet(None,ex.src,ex.sheet,ex.dest,cols,rows,now,now)
      }
      IO.executeDBIOSeq(Excelsheet.insertAction(sheets))

      IO.executeDBPlain(config.postcreatesql)

    } finally {
      session.close
      print("session closed\n")
    }
  }
}
