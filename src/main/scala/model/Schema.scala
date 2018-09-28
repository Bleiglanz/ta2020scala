
package model

object Schema {

  private val document = DBTable("document",List(
    DBString("name"),
    DBString("doctype"),
    DBString("fullpath"),
    DBString("extension"),
    DBLong("size")
  ))

  private val howmanyexcelcomlumns = ta2020.Config.howmanyexcelcomlumns
  private val mergetables = DBTable("mergetable",
    (for(i<-1 to howmanyexcelcomlumns ; col = DBString(s"s$i")) yield col).toList
  )

  val tables:List[DBTable] = List(document,mergetables)
}

