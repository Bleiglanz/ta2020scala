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
import java.io.File

import ta2020.Config

import collection.JavaConverters._
import helper._
//import model.entities.Document

object Starter {

  def main(args: Array[String]): Unit = {

    val config = Config

    implicit val db = config.db

    if (args.contains("gen")) {
      val schema: String = txt.schema_sql(model.Schema.tables).toString()
      writeUTF8File("src/main/resources/schema.sql", schema)

      for (table <- model.Schema.tables) {
        print(schema)
        val code = txt.schema_slick(table).toString()
        print(code)
        writeUTF8File(fname = "src/main/scala/model/entities/" + table.caseclassname + ".scala", code)
      }

      //helper.writeUTF8File("src/main/scala/model/entities/" + table.caseclassname + ".scala", code)
    } else if (args.contains("create")) {
//      IO.executeDBIOSeq(Document.dropAction andThen Document.createAction)
//      val docs = IO.getListOfAllowedFiles(config.inputdirs,config.inputfiles)
//      IO.executeDBIOSeq(Document.insertAction(docs))
    } else {
      //val fs = getListOfAllowedFiles(Config.inputdirs, allowed).map(_.getAbsolutePath)
      val fs = config.inputfiles
      assert(new File(fs.head).exists())
      val data: Array[Array[String]] = ta2020.TableFromExcel.procSingleExcelGeneral("ta2020_", fs.head).asScala.head.getData
      val filtered = data.filter(l => l.take(4).map(_.trim.length).sum > 0)
      helper.writeUTF8File(Config.outputdir + "index.html", html.index(filtered, config).toString())
    }
  }
}