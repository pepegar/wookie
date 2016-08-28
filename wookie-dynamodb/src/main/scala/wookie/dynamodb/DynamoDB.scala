package wookie
package dynamodb

import cats.data.Kleisli
import cats.~>
import com.amazonaws.auth.BasicAWSCredentials
import wookie.dynamodb.algebra._
import wookie.httpclient._
import wookie.result._
import wookie.service._
import wookie.signer._

import scala.concurrent.Future
import scala.language._

import implicits._

case class DynamoDB(props: Properties, client: HttpClient) extends Service {

  def endpoint = "https://dynamodb.us-east-1.amazonaws.com"

  def serviceName = "dynamodb"

  def credentials = new BasicAWSCredentials(props.accessKey, props.secretAccessKey)

  def run[A](op: DynamoDBIO[A]): Future[A] = {
    val result = op foldMap dynamoDBInterpreter

    result.run(Signer(endpoint, serviceName, credentials))
  }

  val dynamoDBInterpreter = new (DynamoDBOp ~> Result) {
    def apply[A](command: DynamoDBOp[A]): Result[A] =
      Kleisli { signer: Signer ⇒
        client.exec(signer.sign(command.marshalledReq))(command.responseHandler, errorResponseHandler)
      }
  }
}
