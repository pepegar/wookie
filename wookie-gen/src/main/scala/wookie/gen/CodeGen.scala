package wookie
package gen

import com.amazonaws.services.dynamodbv2.model._
import java.lang.reflect._
import scala.reflect.ClassTag

case class Service(
  name:       String,
  operations: Map[Class[_], Class[_]]
)

object dynamodb extends Service("DynamoDB", Map(
  classOf[ListTablesRequest] → classOf[ListTablesResult],
  classOf[QueryRequest] → classOf[QueryResult],
  classOf[ScanRequest] → classOf[ScanResult],
  classOf[UpdateItemRequest] → classOf[UpdateItemResult],
  classOf[PutItemRequest] → classOf[PutItemResult],
  classOf[DescribeTableRequest] → classOf[DescribeTableResult],
  classOf[CreateTableRequest] → classOf[CreateTableResult],
  classOf[UpdateTableRequest] → classOf[UpdateTableResult],
  classOf[DeleteTableRequest] → classOf[DeleteTableResult],
  classOf[GetItemRequest] → classOf[GetItemResult],
  classOf[BatchWriteItemRequest] → classOf[BatchWriteItemResult],
  classOf[BatchGetItemRequest] → classOf[BatchGetItemResult],
  classOf[DeleteItemRequest] → classOf[DeleteItemResult]
))

class FreeGen(service: Service) {

  val managed: List[Class[_]] = service.operations.map(_._1).toList
  val results: List[Class[_]] = service.operations.map(_._2).toList

  // These Java classes will have non-Java names in our generated code
  val ClassBoolean = classOf[Boolean]
  val ClassByte = classOf[Byte]
  val ClassShort = classOf[Short]
  val ClassInt = classOf[Int]
  val ClassLong = classOf[Long]
  val ClassFloat = classOf[Float]
  val ClassDouble = classOf[Double]
  val ClassVoid = Void.TYPE

  val renames: Map[Class[_], String] =
    Map(classOf[java.sql.Array] → "SqlArray")

  def tparams(t: Type): List[String] =
    t match {
      case t: GenericArrayType  ⇒ tparams(t.getGenericComponentType)
      case t: ParameterizedType ⇒ t.getActualTypeArguments.toList.flatMap(tparams)
      case t: TypeVariable[_]   ⇒ List(t.toString)
      case _                    ⇒ Nil
    }

  def toScalaType(t: Type): String =
    t match {
      case t: GenericArrayType      ⇒ s"Array[${toScalaType(t.getGenericComponentType)}]"
      case t: ParameterizedType     ⇒ s"${toScalaType(t.getRawType)}${t.getActualTypeArguments.map(toScalaType).mkString("[", ", ", "]")}"
      case t: WildcardType          ⇒ "_" // not quite right but ok
      case t: TypeVariable[_]       ⇒ t.toString
      case ClassVoid                ⇒ "Unit"
      case ClassBoolean             ⇒ "Boolean"
      case ClassByte                ⇒ "Byte"
      case ClassShort               ⇒ "Short"
      case ClassInt                 ⇒ "Int"
      case ClassLong                ⇒ "Long"
      case ClassFloat               ⇒ "Float"
      case ClassDouble              ⇒ "Double"
      case x: Class[_] if x.isArray ⇒ s"Array[${toScalaType(x.getComponentType)}]"
      case x: Class[_]              ⇒ renames.getOrElse(x, x.getSimpleName)
    }

  // Each constructor for our algebra maps to an underlying method, and an index is provided to
  // disambiguate in cases of overloading.
  case class Ctor(origin: Constructor[_], returnType: Class[_], index: Int) {

    // The method name, unchanged
    def mname: String =
      origin.getDeclaringClass.getSimpleName

    // The case class constructor name, capitalized and with an index when needed
    def cname: String = {
      val s = mname(0).toUpper +: mname.drop(1)
      (if (index == 0) s else s"$s$index")
    }

    // Constructor parameter type names
    def cparams: List[String] =
      origin.getGenericParameterTypes.toList.map(toScalaType)

    // Constructor arguments, a .. z zipped with the right type
    def cargs: List[String] =
      "abcdefghijklmnopqrstuvwxyz".toList.zip(cparams).map {
        case (n, t) ⇒ s"$n: $t"
      }

    // Return type name
    def ret: String = toScalaType(returnType)

    // Case class/object declaration
    def ctor(sname: String): String =
      ("|case " + (cparams match {
        case Nil ⇒ s"object $cname"
        case ps  ⇒ s"class $cname(${cargs.mkString(", ")})"
      }) + s""" extends ${service.name}Op[$ret] {
        |    }""").trim.stripMargin

    // Argument list: a, b, c, ... up to the proper arity
    def args: String =
      "abcdefghijklmnopqrstuvwxyz".toList.take(cparams.length).mkString(", ")

    // Pattern to match the constructor
    def pat: String =
      cparams match {
        case Nil ⇒ s"object $cname"
        case ps  ⇒ s"class  $cname(${cargs.mkString(", ")})"
      }

    // Case clause mapping this constructor to the corresponding primitive action
    def prim(sname: String): String =
      (if (cargs.isEmpty)
        s"case $cname => primitive(_.$mname)"
      else
        s"case $cname($args) => primitive(_.$mname($args))")

    // Smart constructor
    def lifted(sname: String): String =
      if (cargs.isEmpty) {
        s"""|/** 
            |   * @group Constructors (Primitives)
            |   */
            |  val $mname: ${sname}IO[$ret] =
            |    F.liftFC(${cname})
         """.trim.stripMargin
      } else {
        s"""|/** 
            |   * @group Constructors (Primitives)
            |   */
            |  def $mname(${cargs.mkString(", ")}): ${sname}IO[$ret] =
            |    F.liftFC(${cname}($args))
         """.trim.stripMargin
      }

  }

  // All method for this class and any superclasses/interfaces
  def constructors[A](c: Class[A]): List[Constructor[_]] = c.getConstructors.toList

  // Ctor values for all constructors in of A plus superclasses, interfaces, etc.
  def ctors[A, B](implicit request: ClassTag[A], returnType: Class[B]): List[Ctor] =
    constructors(request.runtimeClass).groupBy(_.getName).toList.flatMap {
      case (n, ms) ⇒
        ms.toList.zipWithIndex.map {
          case (m, i) ⇒ Ctor(m, returnType, i)
        }
    }.sortBy(_.cname)

  // All types referenced by all constructors on A, superclasses, interfaces, etc.
  def imports[A, B](implicit request: ClassTag[A], returnType: Class[B]): List[String] =
    (s"import ${request.runtimeClass.getName}" :: ctors.map(_.origin).flatMap { m ⇒
      m.getDeclaringClass :: managed.toList.filterNot(_ == request.runtimeClass) ::: results.toList.filterNot(_ == returnType) ::: m.getParameterTypes.toList
    }.map { t ⇒
      if (t.isArray) t.getComponentType else t
    }.filterNot(t ⇒ t.isPrimitive).map { c ⇒
      val sn = c.getSimpleName
      val an = renames.getOrElse(c, sn)
      if (sn == an) s"import ${c.getName}"
      else s"import ${c.getPackage.getName}.{ $sn => $an }"
    }).distinct.sorted

  def module: String = {
    s"""
    |package wookie
    |package ${service.name.toLowerCase}
    |
    |${service.operations.flatMap(tup ⇒ imports(ClassTag(tup._1), tup._2)).toList.distinct.sorted.mkString("\n")}
    |
    |object ast {
    |
    |  sealed trait ${service.name}Op[A] extends Product with Serializable
    |
    |  object ${service.name}Op {
    |    ${service.operations.map(tup ⇒ constructorsForClass(ClassTag(tup._1), tup._2)).mkString}
    |  }
    |}
    |""".trim.stripMargin
  }

  def constructorsForClass[A, B](implicit request: ClassTag[A], result: Class[B]): String = {
    val sname = toScalaType(request.runtimeClass)
    s"""
    |    ${ctors[A, B].map(_.ctor(sname)).mkString("\n    ")}
    |""".trim.stripMargin
  }
}

object Generator extends FreeGen(dynamodb) with App {

  println(module)

}

