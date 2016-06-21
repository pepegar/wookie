package aws
package dynamodb

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import cats.Monad
import cats.data.Kleisli

import com.amazonaws.Request
import com.amazonaws.transform.Marshaller

import scala.concurrent.Future

import language._
import ast._

object DynamoDB extends DynamoDBInstructions {
  import interpreter._
  import implicits._
  import Kleisli._

  type Result[A] = Kleisli[Future, Marshaller[Request[A], A], A]

  implicit val _: Monad[Result] = implicitly

  def endpoint = "https://dynamodb.us-east-1.amazonaws.com"

  def run[A](op: DynamoDBMonad[A])(
    implicit system: ActorSystem,
             mat: ActorMaterializer,
             m: Marshaller[Request[A], A]
  ): Future[A] = {
    val result = op foldMap futureInterpreter(endpoint)

    result.run(m)
  }
}
