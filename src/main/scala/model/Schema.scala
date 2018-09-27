
package model

object Schema {

  private val document = DBTable("document",List(
    DBString("name"),
    DBString("doctype"),
    DBString("fullpath"),
    DBString("extension"),
    DBLong("size")
  ))

  private val mergetables = DBTable("mergetable",
    (for(i<-1 to 50; col = DBString(s"S$i")) yield col).toList
  )

  val tables:List[DBTable] = List(document)
}

