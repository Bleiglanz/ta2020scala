
package object model {

  sealed trait DBTableTrait{
    def name:String
    def fields:List[DBField]
    def caseclassname:String = name.capitalize
    def htmlheader:String
  }
  final case class DBTable(name:String, fields:List[DBField], htmlheader:String = "") extends DBTableTrait

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
