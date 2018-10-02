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
package ta2020

import java.text.DateFormat
import java.util.{Calendar, Locale}

import com.typesafe.config.{Config=>TypesafeConfig, ConfigFactory}

import collection.JavaConverters._
import slick.jdbc.PostgresProfile.api._

trait Configuration{
  def db:Database
  def inputdirs:List[String]
  def inputfiles:List[String]
  def outputdir:String
  def htmltitle:String
  def infosystemTa:String
  def uilinks:List[(String,String)]
  def uihome:String
}

case object Config extends Configuration {

  private val dateformat:DateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.GERMANY)

  private val currentdate:String =dateformat.format(Calendar.getInstance().getTime)

  private val config: com.typesafe.config.Config = ConfigFactory.load()

  val db:Database = Database.forConfig("ta2020.db")

  val inputdirs:List[String] = config.getStringList("ta2020.inputdirs").asScala.toList

  val inputfiles:List[String] = config.getStringList("ta2020.inputfiles").asScala.toList

  val outputdir:String = config.getString("ta2020.outputdir")

  val htmltitle:String = config.getString("ta2020.htmltitle") + currentdate

  val infosystemTa:String = config.getString("ta2020.infosystem_ta")

  val uilinks:List[(String,String)] = config.getObjectList("ta2020.ui.links").asScala.toList.map(_.toConfig).map(c=>(c.getString("text"),c.getString("url")))

  val excel2db:List[ExcelImport] = config.getObjectList("ta2020.excel.imports").asScala.toList.map(c=>ExcelImport(c.toConfig))

  val uihome:String = config.getString("ta2020.ui.home")

  private object ExcelImport {
    def apply(c:TypesafeConfig):ExcelImport = ExcelImport(c.getString("file"),c.getString("sheet"),c.getString("dest"))
  }

  case class ExcelImport(src:String, sheet:String, dest:String)
}


