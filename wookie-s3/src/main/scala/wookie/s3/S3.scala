package wookie
package s3

import service.Service
import scala.concurrent.Future
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.amazonaws.transform._
import com.amazonaws._
import com.amazonaws.auth.BasicAWSCredentials

object S3 extends Service {

  import ast._
  import interpreter._
  import result._
  import marshaller._

  val endpoint = "https://s3.amazonaws.com"

  def credentials = new BasicAWSCredentials("", "")

  def run[A](op: S3Monad[A])(implicit
    system: ActorSystem,
    mat: ActorMaterializer,
    m: Marshaller[Request[A], AmazonWebServiceRequest]): Future[A] = {
    val result = op foldMap s3Interpreter(endpoint)

    result.run(SignMarshaller[A](endpoint, credentials))
  }

}
