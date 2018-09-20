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
import scala.annotation.tailrec

object Starter {

  def main(args: Array[String]): Unit = {

    val config = Config

    if (args.contains("gen")){
      print("Hi\n")
      val schema:String = txt.schema_sql(model.Schema.tables).toString()
      print(schema)
      helper.writeUTF8File("src/main/resources/schema.sql",schema)
      val table = model.Schema.tables.head
      val code = txt.schema_slick(table).toString()
      print(code)
      //helper.writeUTF8File("src/main/scala/model/entities/" + table.caseclassname + ".scala", code)
    }else {
      //val fs = getListOfAllowedFiles(Config.inputdirs, allowed).map(_.getAbsolutePath)
      val fs = config.inputfiles
      assert(new File(fs.head).exists())
      val data: Array[Array[String]] = ta2020.TableFromExcel.procSingleExcelGeneral("ta2020_", fs.head).asScala.head.getData
      val filtered = data.filter(l=>l.take(4).map(_.trim.length).sum >0)
      helper.writeUTF8File(Config.outputdir + "index.html",html.index(filtered, config).toString())
    }
  }


  def allowed(file: File): Boolean = Option(file) match {
    case None => false
    case Some(f) => f.isFile && List(".xls", ".xlsx", ".pdf").exists(f.getName.endsWith)
  }

  def getListOfAllowedFiles(dir: List[String], pred: File => Boolean): List[File] = {

    @tailrec def scanDirs(dirs: List[File], files: List[File]): List[File] = dirs match {
      case Nil => files
      case head :: rest => Option(head.listFiles) match {
        case None => scanDirs(rest, files)
        case Some(l) => scanDirs(l.filter(_.isDirectory).toList ::: rest, l.filter(f => pred(f)).toList ::: files)
      }
    }
    val df = dir.map(new File(_)).filter(_.isDirectory)
    scanDirs(df, Nil)

  }

}