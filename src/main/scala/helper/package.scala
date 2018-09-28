import java.io.{File, PrintWriter}

import ta2020.Configuration

package object helper {

  def writeUTF8File(fname: String, content: String): Unit = {
    val pw = new PrintWriter(new File(fname), "UTF-8")
    pw.print(content)
    pw.close()
  }

  val isExcel:String => Boolean = List("xls", "xlsm", "xlsx").contains(_)

}


