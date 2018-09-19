
package object model {

  sealed trait DBTableTrait{
    def name:String
    def fields:List[DBField]
    def caseclassname:String = name.capitalize
  }
  case class DBTable(name:String,fields:List[DBField]) extends DBTableTrait

  sealed trait DBField{
    def name:String
    def ddl:String
    def sca:String
    def scaladecl:String = name + ":" + sca
  }

  case class DBString(name:String, nullable:Boolean=false) extends DBField{
    override def ddl:String = s"$name varchar(255) ${ifnull(nullable)}"
    override def sca:String = "String"
  }

  case class DBText(name:String, nullable:Boolean=false) extends DBField{
    override def ddl:String = s"$name varchar(2000) ${ifnull(nullable)}"
    override def sca:String = "String"
  }

  case class DBLong(name:String, nullable:Boolean=false) extends DBField{
    override def ddl:String = s"$name bigint ${ifnull(nullable)}"
    override def sca:String = "Long"
  }

  private def ifnull(nullable:Boolean):String = {
    if(nullable) "" else "NOT NULL"
  }

}
