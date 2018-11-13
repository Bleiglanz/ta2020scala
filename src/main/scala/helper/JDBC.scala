// Copyright (C) 2011-2012 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

// http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.package helper
package helper
import java.sql.{Connection, ResultSet}

object JDBC {

  def selectRowtoColumnMaps(sql:String)(implicit conn: Connection):Map[String,String] = {
    if(conn.isClosed) Map(""->"") else {
      val rs:ResultSet =conn.prepareStatement(sql).executeQuery()
      val meta = rs.getMetaData
      val result:scala.collection.mutable.Map[String,String] =scala.collection.mutable.Map.empty
      var rowcount=0
      while(rs.next()){
        rowcount = rowcount + 1
        (1 to meta.getColumnCount) foreach { i =>
           val key = meta.getColumnLabel(i).toUpperCase()
           val betterkey = if(result.contains(key)) s"""${key}${rowcount}""" else key
           result(betterkey)=rs.getString(i) }
      }
      result.toMap
    }
  }

}
