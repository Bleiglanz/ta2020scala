
package model

object Schema {

  private val document = DBTable("document",List(
    DBString("name"),
    DBString("doctype"),
    DBString("fullpath"),
    DBString("extension"),
    DBLong("size")
    )
  )

  private val excelsheet = DBTable("excelsheet",List(
    DBString("filename"),
    DBString("sheetname"),
    DBString("tablename"),
    DBInt("cols"),
    DBInt("rows")
  ))

  private val meldungen = DBTable("meldungen",List(
    DBString("tanr"),
    DBString("shorttext"),
    DBString("longtext"),
  ),
    """
      |     <thead>
      |       <tr>
      |           <th>TANR</th>
      |           <th>NAME</th>
      |           <th>TEXT</th>
      |       </tr>
      |     </thead>
    """.stripMargin)

  private val steckscheiben = DBTable("steckscheiben",List(
    DBString("sysnr"),
    DBString("sysnr2"),
    DBString("grnr"),
    DBString("lfdnr"),
    DBString("beschreibung"),
    DBString("rkl"),
    DBString("dn"),
    DBString("pn"),
    DBString("pid"),
    DBString("apparatenr"),
    DBBoolean("brille"),
    DBBoolean("job"),
    DBBoolean("syssteck")
  ))

  val tables:List[DBTable] = List(document,excelsheet,meldungen,steckscheiben)
}

