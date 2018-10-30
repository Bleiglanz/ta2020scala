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
import tasks.{GenerateDatabaseCode, GenerateWebsite, ImportData, TaskTrait}

object Starter {

  private val tasks:Map[String,TaskTrait] = Map("gen"->GenerateDatabaseCode,"site"->GenerateWebsite,"import"->ImportData)

  def main(args: Array[String]): Unit = {
    args foreach { s =>
      val t:TaskTrait = tasks(s)
      print(s"\nStart ${t.info} ...\n")
      t.run()
      print(s"...${t.info} done.\n")
    }
    ta2020.Config.db.close()
  }
}
