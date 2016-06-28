package wookie
package s3

import scala.concurrent.Future
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.amazonaws.transform._
import com.amazonaws._
import com.amazonaws.auth.BasicAWSCredentials

import service._

case class S3(props: Properties) extends Service {

  import ast._
  import interpreter._
  import result._
  import signer._
  import marshallable._

  val endpoint = "https://s3.amazonaws.com"

  val serviceName = "s3"

  def credentials = new BasicAWSCredentials(
    props.accessKey,
    props.secretAccessKey
  )

  def run[A](op: S3Monad[A])(implicit
    system: ActorSystem,
    mat: ActorMaterializer): Future[A] = {
    val result = op foldMap s3Interpreter(endpoint)

    result.run(Signer[A](endpoint, serviceName, credentials))
  }

}
