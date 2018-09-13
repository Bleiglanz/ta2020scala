package ta2020

import com.typesafe.config.{Config, ConfigFactory}
import collection.JavaConverters._


object Configuration {

  val config: Config = ConfigFactory.load()

  val directory:List[String] = config.getStringList("ta2020.dir").asScala.toList

}
