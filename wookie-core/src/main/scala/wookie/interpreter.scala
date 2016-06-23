package wookie

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResponse
import com.amazonaws.Request
import com.amazonaws.http.HttpResponseHandler

import cats.data.Xor

import http._

object interpreter {
  trait Interpreter {

    def send[A, B]
      (endpoint: String, request: Request[A])
      (implicit
       handler: HttpResponseHandler[AmazonWebServiceResponse[B]],
       system: ActorSystem,
       mat: ActorMaterializer
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
