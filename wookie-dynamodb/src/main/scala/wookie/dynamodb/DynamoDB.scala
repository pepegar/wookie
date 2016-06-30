package wookie
package dynamodb

import com.amazonaws.{ AmazonWebServiceRequest, Request }
import com.amazonaws.transform.Marshaller
import com.amazonaws.auth.BasicAWSCredentials

import scala.concurrent.Future

import cats.~>
import cats.data.Kleisli

import result._
import service._
import language._
import ast._
import marshallable._
import signer._
import httpclient._

case class DynamoDB(props: Properties, client: HttpClient) extends Service {
  import cats.std.future._

  def endpoint = "https://dynamodb.us-east-1.amazonaws.com"

  def serviceName = "dynamodb"

  def credentials = new BasicAWSCredentials(props.accessKey, props.secretAccessKey)

  def run[A](op: DynamoDBMonad[A]): Future[A] = {
    val result = op foldMap dynamoDBInterpreter

    result.run(Signer[A](endpoint, serviceName, credentials))
  }

  val dynamoDBInterpreter = new (DynamoDBOp ~> Result) {
    def apply[A](command: DynamoDBOp[A]): Result[A] =
      Kleisli { signer: Signer[A] ⇒
        client.exec(signer.sign(command.req))(command.responseHandler)
      }
  }
}
