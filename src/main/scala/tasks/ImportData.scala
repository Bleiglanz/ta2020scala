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
import ta2020.{Config, ExcelImport}
import model.entities.{Document, Excelsheet, Meldungen, Steckscheiben, Tikadocument}
import slick.jdbc.PostgresProfile
import java.sql.Timestamp
import java.time.Instant
import java.io.File
import java.nio.file.Files

case object ImportData extends TaskTrait {

  val info = "importing data to local database from excel sheets"

  override def run(): Unit = {

    val config = Config
    val now:Timestamp = Timestamp.from(Instant.now)

    implicit val db: PostgresProfile.api.Database = config.db

    val presource = scala.io.Source.fromFile(config.precreatesql)
    IO.executeDBPlain(presource.mkString)
    presource.close()

    IO.executeDBIOSeq(Document.dropAction andThen Document.createAction)
    IO.executeDBIOSeq(Excelsheet.dropAction andThen Excelsheet.createAction)
    IO.executeDBIOSeq(Meldungen.dropAction andThen Meldungen.createAction)
    IO.executeDBIOSeq(Steckscheiben.dropAction andThen Steckscheiben.createAction)
    IO.executeDBIOSeq(Tikadocument.dropAction andThen Tikadocument.createAction)

    val session = db.createSession
    try {
      val sheets = config.excel2db.map {
        ex:ExcelImport =>
          val (cols,rows) = ta2020.TableFromExcel.procSingleExcelGeneral("ex_", ex.src, ex.sheet, ex.dest, session.conn, ex.header)
          val timestamp = java.sql.Timestamp.from(Files.getLastModifiedTime(new File(ex.src).toPath).toInstant)
          model.entities.Excelsheet(None,ex.src,ex.sheet,ex.dest,cols,rows,timestamp,now,now)
      }
      IO.executeDBIOSeq(Excelsheet.insertAction(sheets))

      config.excelmerge.foreach { dirname =>
        print("MERGE ALL SHEETS IN: " + dirname + "\n")
        val (_,_) = ta2020.TableFromExcel.procMergeExcel("ex_", dirname, session.conn)
      }

      print("start crawling directories....\n")
      IO.uploadDocumentsFromDir(config.scandirs,config.scanfiles)
      print(s"crawling: done\n")

      val postsource = scala.io.Source.fromFile(config.postcreatesql)
      IO.executeDBPlain(postsource.mkString)
      postsource.close()

    } finally {
      session.close
    }
  }
}
