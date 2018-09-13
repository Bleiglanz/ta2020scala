
import java.io.File

import scala.annotation.tailrec

object Starter {
  def main(args: Array[String]): Unit = {
    val fs = getListOfAllowedFiles("/home/anton/Downloads/",_=>true).map(_.getAbsolutePath)
    fs.foreach(println)
  }

  def allowed(fname: String): Boolean = List(".xls", ".xlsx").exists(fname.endsWith)

  def getListOfAllowedFiles(dir: String, pred: String => Boolean): List[File] = {

    @tailrec def scanDirs(dirs: List[File], files: List[File]): List[File] = dirs match {
      case Nil => files
      case head :: rest =>
        val newfiles: List[File] = head.listFiles.filter(f => allowed(f.getName)).toList
        val newdirs: List[File] = head.listFiles.filter(_.isDirectory).toList
        println(files)
        scanDirs(newdirs ::: rest, newfiles ::: files)
    }

    if (dir == "") Nil else {
      val d = new File(dir)
      println(d.getAbsoluteFile)
      if (d.exists && d.isDirectory) {
        scanDirs(List(d), Nil)
      } else {
        if (allowed(d.getName)) List(d) else Nil
      }
    }
  }
}