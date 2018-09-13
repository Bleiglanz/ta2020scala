package ta2020

import com.typesafe.config.{Config, ConfigFactory}

object Configuration {

  val config: Config = ConfigFactory.load()

  val directory:String = config.getString("ta2020.dir")

}
