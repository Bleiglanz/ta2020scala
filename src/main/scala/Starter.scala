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
import java.io.{File, PrintWriter}

import ta2020.{Configuration, Sidebar}
import ta2020.TADatabase.User

import collection.JavaConverters._
import scala.annotation.tailrec

object Starter {

  def main(args: Array[String]): Unit = {

    if (args.contains("help")) print("Scan files relevat for TA2020 MUO")

    val fs = getListOfAllowedFiles(Configuration.inputdirs, allowed).map(_.getAbsolutePath)

    val data:Array[Array[String]] = fs.filter(_.endsWith("xlsx")).flatMap(ta2020.TableFromExcel.procSingleExcelGeneral("ta2020_", _).asScala).head.getData

    val pw = new PrintWriter(new File( Configuration.outputdir + "/index.html"), "UTF-8")
    pw.print(html.index(Configuration.htmltitle, Sidebar.entries,data))
    pw.close()
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