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
package object model {

  sealed trait DBTableTrait{
    def name:String
    def fields:List[DBField]
    def caseclassname:String = name.capitalize
  }
  final case class DBTable(name:String, fields:List[DBField]) extends DBTableTrait

  sealed trait DBField{
    def name:String
    def sca:String
    def scaladecl:String = name + ":" + sca
  }

  final case class DBString(name:String, nullable:Boolean=false) extends DBField{
    override def sca:String = "String"
  }

  final case class DBLong(name:String, nullable:Boolean=false) extends DBField{
    override def sca:String = "Long"
  }

  final case class DBInt(name:String, nullable:Boolean=false) extends DBField{
    override def sca:String = "Int"
  }

  final case class DBBoolean(name:String, nullable:Boolean=false) extends DBField{
    override def sca:String = "Boolean"
  }
}
