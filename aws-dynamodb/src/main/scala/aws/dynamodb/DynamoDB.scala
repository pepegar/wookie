package aws
package dynamodb

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import cats.Monad
import cats.data.Kleisli
import com.amazonaws.{ AmazonWebServiceRequest, Request }
import com.amazonaws.transform.Marshaller
import com.amazonaws.auth.BasicAWSCredentials

import scala.concurrent.Future

import aws.service._
import language._
import ast._
import marshaller._

object DynamoDB extends Service with DynamoDBInstructions {
  import interpreter._
  import cats.std.future._
  import Kleisli._

  type Result[A] = Kleisli[Future, SignMarshaller[A], A]

  implicit val _: Monad[Result] = implicitly

  def endpoint = "https://dynamodb.us-east-1.amazonaws.com"

  def credentials = new BasicAWSCredentials("", "")

  def run[A]
    (op: DynamoDBMonad[A])
    (implicit system: ActorSystem,
              mat: ActorMaterializer,
              m: Marshaller[Request[A], AmazonWebServiceRequest]
  ): Future[A] = {
    val result = op foldMap dynamoDBInterpreter(endpoint)

    result.run(SignMarshaller[A](endpoint, credentials))
  }
}
