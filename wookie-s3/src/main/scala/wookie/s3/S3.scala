package wookie
package s3

import cats.data.Kleisli
import cats.~>
import com.amazonaws.auth.BasicAWSCredentials
import wookie.httpclient._
import wookie.result._
import wookie.s3.algebra._
import wookie.service._
import wookie.signer._

import scala.concurrent.Future

case class S3(props: Properties, client: HttpClient) extends Service {

  val endpoint = "https://s3.amazonaws.com"

  val serviceName = "s3"

  def credentials = new BasicAWSCredentials(
    props.accessKey,
    props.secretAccessKey
  )

  def run[A](op: S3IO[A]): Future[A] = {
    val result = op foldMap s3Interpreter

    result.run(Signer(endpoint, serviceName, credentials))
  }

  val s3Interpreter = new (S3Op ~> Result) {
    def apply[A](command: S3Op[A]): Result[A] =
      Kleisli { signer: Signer ⇒
        client.exec(signer.sign(command.marshalledReq))(command.responseHandler)
      }
  }

}
