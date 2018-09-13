
import java.io.File

import ta2020.Configuration

import scala.annotation.tailrec

object Starter {
  def main(args: Array[String]): Unit = {
    val fs = getListOfAllowedFiles(Configuration.directory,allowed).map(_.getAbsolutePath)
    fs.foreach(println)
  }

  def allowed(fname: String): Boolean = List(".xls", ".xlsx").exists(fname.endsWith)

  def getListOfAllowedFiles(dir: List[String], pred: String => Boolean): List[File] = {

    @tailrec def scanDirs(dirs: List[File], files: List[File]): List[File] = dirs match {
      case Nil => files
      case head :: rest =>
        val newfiles: List[File] = {
          head.listFiles.filter(f => pred(f.getName)).toList
        }
        val newdirs: List[File] = head.listFiles.filter(_.isDirectory).toList
        scanDirs(newdirs ::: rest, newfiles ::: files)
    }

    val df = dir.map(new File(_)).filter(_.isDirectory)
   scanDirs(df, Nil)

  }

}