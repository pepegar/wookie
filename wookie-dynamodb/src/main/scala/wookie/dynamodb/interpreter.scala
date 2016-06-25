package wookie
package dynamodb

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import cats.data.{ Xor, Kleisli }
import cats.~>
import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResponse
import com.amazonaws.Request
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.http.HttpResponseHandler
import com.amazonaws.transform.Marshaller
import scala.concurrent.Future

object interpreter extends Interpreter {
  import ast._
  import marshaller._
  import DynamoDB._
  import service._

  def dynamoDBInterpreter(endpoint: String)(
    implicit
    system: ActorSystem,
    mat: ActorMaterializer
  ) = new (DynamoDBOp ~> Result) {
    def apply[A](command: DynamoDBOp[A]): Result[A] =
      Kleisli { marshaller: SignMarshaller[A] =>
        send(endpoint, marshaller.marshallAndSign(command.req, marshaller.credentials))(command.responseHandler, system, mat)
      }
  }

}
