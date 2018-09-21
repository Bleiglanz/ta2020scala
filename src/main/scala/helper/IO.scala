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
// limitations under the License.
package helper

import java.io.File

import scala.annotation.tailrec

object IO {

  private def allowed(file: File): Boolean = Option(file) match {
    case None => false
    case Some(f) => f.exists && f.isFile
  }

  def getListOfAllowedFiles(dirs: List[String], files: List[String], pred: File => Boolean = allowed): List[File] = {

    @tailrec def scanDirs(dirs: List[File], files: List[File]): List[File] = dirs match {
      case Nil => files
      case head :: rest => Option(head.listFiles) match {
        case None => scanDirs(rest, files)
        case Some(l) => scanDirs(l.filter(_.isDirectory).toList ::: rest, l.filter(f => pred(f)).toList ::: files)
      }
    }
    val df = dirs.map(new File(_)).filter(_.isDirectory)
    scanDirs(df, files.map(new File(_)).filter(pred))
  }
}
