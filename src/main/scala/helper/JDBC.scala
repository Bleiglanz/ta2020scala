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

import scala.collection.mutable.ListBuffer

object JDBC {

  def selectRowtoColumnMaps(sql:String)(implicit conn: Connection):Map[String,String] = {
    if(conn.isClosed) Map(""->"") else {
      val rs:ResultSet =conn.prepareStatement(sql).executeQuery()
      val meta = rs.getMetaData
      val res = new ListBuffer[(String,String)]
      while(rs.next()){
        for(i <- 1 to meta.getColumnCount)
        { res += (meta.getColumnLabel(i).toUpperCase()->rs.getString(i))}
      }
      res.toMap
    }
  }

}
