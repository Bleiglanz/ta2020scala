
import java.io.File

import scala.annotation.tailrec

object Starter {
  def main(args: Array[String]): Unit = {
  }

  def allowed(fname:String):Boolean = List(".xls",".xlsx").exists(fname.endsWith)

  def getListOfAllowedFiles(dir: String, pred: String => Boolean):List[File] = {

    @tailrec def scanDirs(dirs:List[File],files:List[File] = ???

    if(dir=="") Nil else {
      val d = new File(dir)
      if (d.exists && d.isDirectory) {
        d.listFiles.toList
      } else {
        List[File]()
      }
    }
  }
}