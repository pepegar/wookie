package aws
package dynamodb

import cats.~>
import cats.data.{Xor, Kleisli}
import com.amazonaws.AmazonWebServiceRequest
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.http.HttpResponseHandler
import com.amazonaws.AmazonWebServiceResponse
import com.amazonaws.Request
import com.amazonaws.transform.Marshaller
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object interpreter {
  import ast._
  import http._
  import DynamoDB._

  def futureInterpreter(endpoint: String)(
    implicit
    system: ActorSystem,
    mat: ActorMaterializer
  ) = new (DynamoDBOp ~> Result) {
    def apply[A](command: DynamoDBOp[A]): Result[A] =
      Kleisli { marshaller: Marshaller[Request[A], AmazonWebServiceRequest] =>
        send(marshaller.marshall(command.req))(command.responseHandler)
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
