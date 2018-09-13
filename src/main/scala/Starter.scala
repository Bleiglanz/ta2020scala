
import java.io.File

import ta2020.Configuration

import scala.annotation.tailrec

object Starter {
  def main(args: Array[String]): Unit = {
    val fs = getListOfAllowedFiles(Configuration.directory,allowed).map(_.getAbsolutePath)
    fs.foreach{ f=>
      val len = f.length
      val lenstr = f"$len%4s"
      println(s"$lenstr;$f")
    }
    //ta2020.TADatabase.create

  }

  def allowed(f:File): Boolean = {
    if (null==f) false else {
      val fname = f.getName
      List(".xls", ".xlsx", ".pdf").exists(fname.endsWith)
    }
  }

  def getListOfAllowedFiles(dir: List[String], pred: File => Boolean): List[File] = {

    @tailrec def scanDirs(dirs: List[File], files: List[File]): List[File] = dirs match {
      case Nil => files
      case head :: rest =>
        val liste = head.listFiles
        val newfiles: List[File] = {
          if(null==liste) Nil else head.listFiles.filter(f => pred(f)).toList
        }
        val newdirs: List[File] = {
          if(null==liste) Nil else head.listFiles.filter(_.isDirectory).toList
        }
        scanDirs(newdirs ::: rest, newfiles ::: files)
    }

    val df = dir.map(new File(_)).filter(_.isDirectory)
   scanDirs(df, Nil)

  }

}