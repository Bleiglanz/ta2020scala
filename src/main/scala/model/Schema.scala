
package model

object Schema {

  private val documents = DBTable("dokumente",List(
    DBText("name"),
    DBString("fullpath"),
    DBString("extension"),
    DBLong("size")
  ))

  private val mergetables = DBTable("mergetable",
    (for(i<-1 to 50; col = DBText(s"S$i")) yield col).toList
  )

  val tables:List[DBTable] = List(documents, mergetables)
}

