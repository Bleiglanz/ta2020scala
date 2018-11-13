// Copyright (C) 2011-2012 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package model

object Schema {

  private val document = DBTable("document", List(
    DBString("name"),
    DBString("doctype"),
    DBString("fullpath"),
    DBString("extension"),
    DBLong("size"),
    DBString("tanr"),
    DBTimestamp("file_last_modified")
  ))

  private val excelsheet = DBTable("excelsheet", List(
    DBString("filename"),
    DBString("sheetname"),
    DBString("tablename"),
    DBInt("cols"),
    DBInt("rows")
  ))

  private val meldungen = DBTable("meldungen", List(
    DBString("tanr"),
    DBString("shorttext"),
    DBString("longtext")
  ))

  private val steckscheiben = DBTable("steckscheiben", List(
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

  val tables: List[DBTable] = List(document, excelsheet, meldungen, steckscheiben)
}
