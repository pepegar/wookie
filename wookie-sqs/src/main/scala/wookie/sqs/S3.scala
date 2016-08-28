package wookie.sqs

import cats.data.Kleisli
import cats.~>
import com.amazonaws.auth.BasicAWSCredentials
import wookie.httpclient._
import wookie.result._
import wookie.service._
import wookie.signer._
import wookie.sqs.algebra.{ SQSIO, SQSOp }
import wookie.sqs.implicits._

import scala.concurrent.Future

case class SQS(props: Properties, client: HttpClient) extends Service {

  val endpoint = "https://sqs.amazonaws.com"

  val serviceName = "sqs"

  def credentials = new BasicAWSCredentials(
    props.accessKey,
    props.secretAccessKey
  )

  def run[A](op: SQSIO[A]): Future[A] = {
    val result = op foldMap sqsInterpreter

    result.run(Signer(endpoint, serviceName, credentials))
  }

  val sqsInterpreter = new (SQSOp ~> Result) {
    def apply[A](command: SQSOp[A]): Result[A] =
      Kleisli { signer: Signer ⇒
        client.exec(signer.sign(command.marshalledReq))(command.responseHandler, errorResponseHandler)
      }
  }

}
