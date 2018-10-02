
package model

object Schema {

  private val document = DBTable("document",List(
    DBString("name"),
    DBString("doctype"),
    DBString("fullpath"),
    DBString("extension"),
    DBLong("size")
  ))

  private val excelsheet = DBTable("excelsheet",List(
    DBString("filename"),
    DBString("sheetname"),
    DBString("tablename"),
    DBInt("cols"),
    DBInt("rows")
  ))

  val tables:List[DBTable] = List(document,excelsheet)
}

