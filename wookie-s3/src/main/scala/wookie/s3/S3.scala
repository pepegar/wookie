package wookie
package s3

import scala.concurrent.Future

import com.amazonaws.transform._
import com.amazonaws._
import com.amazonaws.auth.BasicAWSCredentials

import cats.~>
import cats.data.Kleisli

import service._
import httpclient._

case class S3(props: Properties, client: HttpClient) extends Service {

  import ast._
  import result._
  import signer._
  import marshallable._

  val endpoint = "https://s3.amazonaws.com"

  val serviceName = "s3"

  def credentials = new BasicAWSCredentials(
    props.accessKey,
    props.secretAccessKey
  )

  def run[A](op: S3Monad[A]): Future[A] = {
    val result = op foldMap s3Interpreter

    result.run(Signer(endpoint, serviceName, credentials))
  }

  val s3Interpreter = new (S3Op ~> Result) {
    def apply[A](command: S3Op[A]): Result[A] =
      Kleisli { signer: Signer ⇒
        client.exec(signer.sign(command.req))(command.responseHandler)
      }
  }

}
