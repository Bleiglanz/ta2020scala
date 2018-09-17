
import java.io.File

import ta2020.Configuration
import ta2020.TADatabase.User
import collection.JavaConverters._


import scala.annotation.tailrec

object Starter {

  def main(args: Array[String]): Unit = {

    if (args.contains("help")) print("Scan files relevat for TA2020 MUO")

    val fs = getListOfAllowedFiles(Configuration.directory, allowed).map(_.getAbsolutePath)

    fs.filter(_.endsWith("xlsx")).foreach { f =>
        val res = ta2020.TableFromExcel.procSingleExcelGeneral("ta2020_", f).asScala
        for(t <- res;
            o = Option(t);
            f <- o) {
            print(f.getName)
            print(s"\n=============\n")
            for(l <- f.getData) print(l.mkString("--->",",","\n"))

        }


        //for(l <- res2) {print(res.mkString("",",","\n"))}
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