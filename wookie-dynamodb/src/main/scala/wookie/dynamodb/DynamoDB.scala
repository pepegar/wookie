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
import marshallable._
import signer._

case class DynamoDB(props: Properties) extends Service {
  import interpreter._
  import cats.std.future._

  def endpoint = "https://dynamodb.us-east-1.amazonaws.com"

  def serviceName = "dynamodb"

  def credentials = new BasicAWSCredentials(props.accessKey, props.secretAccessKey)

  def run[A](op: DynamoDBMonad[A])(implicit
    system: ActorSystem,
    mat: ActorMaterializer): Future[A] = {
    val result = op foldMap dynamoDBInterpreter(endpoint)

    result.run(Signer[A](endpoint, serviceName, credentials))
  }
}
