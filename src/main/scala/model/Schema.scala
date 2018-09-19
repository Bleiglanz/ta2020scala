
package model

object Schema {

  private val documents = DBTable("dokumente",List(
    DBText("name"),
    DBString("fullpath"),
    DBString("extension"),
    DBLong("size")
  ))

  val tables:List[DBTable] = List(documents)
}

