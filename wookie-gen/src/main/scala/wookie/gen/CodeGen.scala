package wookie
package gen

import java.lang.reflect._

import com.amazonaws.services.dynamodbv2.model._
import com.amazonaws.services.s3.model._

import scala.reflect.ClassTag

case class Service(
  name:       String,
  operations: List[(Class[_], Class[_])]
)

case class CodeGen(service: Service) {

  val managed = service.operations.map(_._1)
  val results = service.operations.map(_._2)

  // These Java classes will have non-Java names in our generated code
  val ClassBoolean = classOf[Boolean]
  val ClassByte = classOf[Byte]
  val ClassShort = classOf[Short]
  val ClassInt = classOf[Int]
  val ClassLong = classOf[Long]
  val ClassFloat = classOf[Float]
  val ClassDouble = classOf[Double]
  val ClassVoid = Void.TYPE

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
      case x: Class[_]              ⇒ x.getSimpleName
    }

  // Each constructor for our algebra maps to an underlying method, and an index is provided to
  // disambiguate in cases of overloading.
  case class Ctor(origin: Constructor[_], returnType: Class[_], index: Int) {

    // The method name, unchanged
    def mname: String =
      origin.getDeclaringClass.getSimpleName

    def scname: String =
      mname(0).toLower +: mname.drop(1)

    // The case class constructor name, capitalized and with an index when needed
    def cname: String = {
      val s = (mname(0).toUpper +: mname.drop(1)).replaceAll("Request", "")
      (if (index == 0) s else s"$s$index")
    }

    // Constructor parameter type names
    def cparams: List[String] =
      origin.getGenericParameterTypes.toList.map(toScalaType)

    def ctparams: String = {
      val ss = origin.getGenericParameterTypes.toList.flatMap(tparams).toSet
      if (ss.isEmpty) "" else ss.mkString("[", ", ", "]")
    }

    val letters = "abcdefghijklmnopqrstuvwxyz"

    // Constructor arguments, a .. z zipped with the right type
    def cargs: List[String] =
      letters.toList.zip(cparams).map {
        case (n, t) ⇒ s"$n: $t"
      }

    // Return type name
    def ret: String = toScalaType(returnType)

    // Case class/object declaration
    def ctor(sname: String): String =
      (s"""
        |case class $cname(${cargs.mkString(", ")})(implicit M: Marshaller[Request[$mname], $mname], RH: HttpResponseHandler[AmazonWebServiceResponse[$ret]])
        |      extends ${service.name}Op[$ret] {
        |      def amzReq = new $mname(${letters.take(cargs.length).mkString(", ")})
        |      def marshalledReq = M.marshall(amzReq)
        |      def responseHandler = RH
        |    }""").trim.stripMargin

    // Argument list: a, b, c, ... up to the proper arity
    def args: String =
      letters.toList.take(cparams.length).mkString(", ")

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
        s"""|  def $scname: ${service.name}IO[$ret] =
            |        Free.liftF(${cname}())
         """.trim.stripMargin
      } else {
        s"""|  def $scname(${cargs.mkString(", ")}): ${service.name}IO[$ret] =
            |        Free.liftF(${cname}($args))
         """.trim.stripMargin
      }

    def liftedSignature(sname: String): String = {
      if (cargs.isEmpty) {
        s"""|  def $scname: P[$ret]
         """.trim.stripMargin
      } else {
        s"""|  def $scname(${cargs.mkString(", ")}): P[$ret]
         """.trim.stripMargin
      }
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
    (s"import ${request.runtimeClass.getPackage.getName}._" :: ctors.map(_.origin).flatMap { m ⇒
      m.getDeclaringClass :: m.getParameterTypes.toList
    }.map { t ⇒
      if (t.isArray) t.getComponentType else t
    }.filterNot(t ⇒ t.isPrimitive)
      .filterNot(_.getPackage.getName == request.runtimeClass.getPackage.getName)
      .map { c ⇒
        s"import ${c.getName}"
      }).distinct.sorted

  def module: String = {
    s"""
    |package wookie
    |package ${service.name.toLowerCase}
    |
    |import com.amazonaws.http.HttpResponseHandler
    |import com.amazonaws.AmazonWebServiceResponse
    |import com.amazonaws.Request
    |import com.amazonaws.transform.Marshaller
    |${service.operations.flatMap(tup ⇒ imports(ClassTag(tup._1), tup._2)).toList.distinct.sorted.mkString("\n")}
    |import ${service.name.toLowerCase}.implicits._
    |
    |import cats.free.Free
    |
    |object algebra {
    |
    |  import handler._
    |
    |  type ${service.name}IO[A] = Free[${service.name}Op, A]
    |
    |  sealed trait ${service.name}Op[A]
    |    extends Handler[A]
    |    with Product with Serializable {
    |    def marshalledReq: Request[_]
    |  }
    |
    |  object ${service.name}Op {
    |
    |${service.operations.map(tup ⇒ constructorsForClass(ClassTag(tup._1), tup._2)).mkString}
    |  }
    |
    |  object ops {
    |
    |    import ${service.name}Op._
    |
    |    trait ${service.name}Ops[P[_]]{
    |${service.operations.map(tup ⇒ smartConstructorSignatures(ClassTag(tup._1), tup._2)).mkString}
    |    }
    |
    |    object ${service.name}Ops extends ${service.name}Ops[${service.name}IO]{
    |${service.operations.map(tup ⇒ smartConstructors(ClassTag(tup._1), tup._2)).mkString}
    |    }
    |  }
    |
    |}
    |""".trim.stripMargin
  }

  def smartConstructors[A, B](implicit request: ClassTag[A], result: Class[B]): String = {
    val sname = toScalaType(request.runtimeClass)
    s"""
    |    ${ctors[A, B].map(_.lifted(sname)).mkString("\n    ")}
    |""".trim.stripMargin
  }

  def smartConstructorSignatures[A, B](implicit request: ClassTag[A], result: Class[B]): String = {
    val sname = toScalaType(request.runtimeClass)
    s"""
    |    ${ctors[A, B].map(_.liftedSignature(sname)).mkString("\n    ")}
    |""".trim.stripMargin
  }

  def constructorsForClass[A, B](implicit request: ClassTag[A], result: Class[B]): String = {
    val sname = toScalaType(request.runtimeClass)
    s"""
    |    ${ctors[A, B].map(_.ctor(sname)).mkString("\n    ")}
    |""".trim.stripMargin
  }
}

object Generator extends App {

  val service = args.toList match {
    case module :: _ if module == "dynamodb" ⇒ Some(Service("DynamoDB", List(
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
    )))
    case module :: _ if module == "s3" ⇒ Some(Service("S3", List(
      classOf[ListBucketsRequest] → classOf[java.util.List[Bucket]],
      classOf[CreateBucketRequest] → classOf[Unit],
      classOf[DeleteBucketRequest] → classOf[Unit],
      classOf[PutObjectRequest] → classOf[ObjectMetadata],
      classOf[GetObjectRequest] → classOf[S3Object],
      classOf[DeleteObjectRequest] → classOf[Unit],
      classOf[ListObjectsRequest] → classOf[ObjectListing]
    )))
    case Nil ⇒ None
  }

  import java.io.PrintWriter

  service
    .map(s ⇒ (CodeGen(s), s.name))
    .map(t ⇒ (t._1.module, t._2))
    .foreach { t ⇒
      val contents = t._1
      val name = t._2

      new PrintWriter(s"wookie-${name.toLowerCase}/src/main/scala/wookie/${name.toLowerCase}/algebra.scala") { write(contents); close }
    }

}
