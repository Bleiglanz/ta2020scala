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
package helper

import java.io.{File, FileInputStream, PrintWriter}
import java.time.Instant

import model.entities.Document
import slick.dbio.DBIOAction
import java.nio.file.Files

//import org.apache.tika.Tika;
import scala.annotation.tailrec
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


object IO {

  //private val tika:Tika = new Tika

  private val config = ta2020.Config

  def writeUTF8File(fname: String, content: String): Unit = {
    val f: File = new File(fname)
    f.getParentFile.mkdirs()
    val pw = new PrintWriter(f, "UTF-8")
    pw.print(content)
    pw.close()
  }

  val isExcel: String => Boolean = List("xls", "xlsm", "xlsx").contains(_)

  val isPDF: String => Boolean = List("PDF","pdf").contains(_)

  private def fileAllowed(file: File): Boolean = Option(file) match {
    case None => false
    case Some(f) => f.exists && f.isFile
  }

  def uploadDocumentsFromDir(dirs: List[String], filenames: List[String], pred: File => Boolean = fileAllowed)(implicit db:Database):Unit = {

    def makedoc(f: File): Document = {
      val name = f.getName
      val extension = name.split('.').last.toLowerCase()
      val regex = """([0-9]{4})[.-][0-9]{3}""".r()
      val tanr: String = regex.findFirstIn(name).getOrElse("").replace('-', '.')
      val text:String = if (config.extractText.contains(extension)) {
//        try {
//          val fs:FileInputStream = new FileInputStream(f)
//          println(s"parse Datei $name")
//          tika.parseToString(fs)
//        } catch {
//          case _: Exception => "Fehler beim Extrahieren von Text"
//        } finally {
//        }
        "tika"
      }else{
        ""
      }
      Document(None,
        name,
        "",
        f.getAbsolutePath,
        extension
        , f.length, tanr,
        java.sql.Timestamp.from(Files.getLastModifiedTime(f.toPath).toInstant),
        text,
        java.sql.Timestamp.from(Instant.now),
        java.sql.Timestamp.from(Instant.now))
    }

    @tailrec def scanDirs(dirs: List[File], filelist: List[File]): List[File] = dirs match {
      case Nil => filelist
      case head :: rest => Option(head.listFiles) match {
        case None => scanDirs(rest, filelist)
        case Some(l) => scanDirs(l.filter(_.isDirectory).toList ::: rest, l.filter(f => pred(f)).toList ::: filelist)
      }
    }

    val df = dirs.map(new File(_)).filter(_.isDirectory)
    val filelist = scanDirs(df, filenames.map(new File(_)).filter(pred))
    for(f <- filelist) {
      val doc= makedoc(f)
      IO.executeDBIOSeq(Document.insertAction(Seq(doc)))
    }

  }

  def executeDBIOSeq(actions: DBIOAction[Unit, NoStream, _])(implicit db: Database): Unit = {
    val timeout = 25.seconds
    val f: Future[Unit] = db.run(actions)
    Await.result(f, timeout)
  }

  def executeDBIOQuery[T](actions: DBIO[Seq[T]])(implicit db: Database): Seq[T] = {
    val timeout = 25.seconds
    val f: Future[Seq[T]] = db.run(actions)
    Await.result(f, timeout)
  }

  def executeDBPlain(sql: String)(implicit db: Database): Unit = {
    try {
      val conn = db.source.createConnection()
      println(s"executesql: ${sql.take(10)} \n")
      conn.prepareStatement(sql).execute()
      conn.close()
    } catch {
      case e: Throwable => e.printStackTrace()
    }
  }
}
