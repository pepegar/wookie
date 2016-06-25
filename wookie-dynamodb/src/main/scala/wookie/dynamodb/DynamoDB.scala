package wookie
package dynamodb

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import com.amazonaws.{ AmazonWebServiceRequest, Request }
import com.amazonaws.transform.Marshaller
import com.amazonaws.auth.BasicAWSCredentials

import scala.concurrent.Future

import result._
import service._
import language._
import ast._
import marshaller._

object DynamoDB extends Service {
  import interpreter._
  import cats.std.future._

  def endpoint = "https://dynamodb.us-east-1.amazonaws.com"

  def credentials = new BasicAWSCredentials("", "")

  def run[A](op: DynamoDBMonad[A])(implicit
    system: ActorSystem,
    mat: ActorMaterializer,
    m: Marshaller[Request[A], AmazonWebServiceRequest]): Future[A] = {
    val result = op foldMap dynamoDBInterpreter(endpoint)

    result.run(SignMarshaller[A](endpoint, credentials))
  }
}
