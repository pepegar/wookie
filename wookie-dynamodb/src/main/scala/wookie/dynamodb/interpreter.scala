package wookie 
package dynamodb

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import cats.data.{Xor, Kleisli}
import cats.~>
import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResponse
import com.amazonaws.Request
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.http.HttpResponseHandler
import com.amazonaws.transform.Marshaller
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object interpreter {
  import ast._
  import http._
  import marshaller._
  import DynamoDB._

  def dynamoDBInterpreter(endpoint: String)(
    implicit
    system: ActorSystem,
    mat: ActorMaterializer
  ) = new (DynamoDBOp ~> Result) {
    def apply[A](command: DynamoDBOp[A]): Result[A] =
      Kleisli { marshaller: SignMarshaller[A] =>
        send(marshaller.marshallAndSign(command.req, marshaller.credentials))(command.responseHandler)
      }

    def send[A, B]
      (request: Request[A])
      (implicit handler: HttpResponseHandler[AmazonWebServiceResponse[B]]
    ): Future[B] = {
      for {
        httpResponse <- sendRequest(createHttpRequest(request))
        marshalledResponse <- parseResponse(endpoint, httpResponse)
      } yield marshalledResponse match {
        case Xor.Right(resp) => resp
        case Xor.Left(exc) => throw exc
      }
    }
  }

}
