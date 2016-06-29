package wookie
package akkahttp

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import com.amazonaws.{ Request, AmazonWebServiceResponse }
import com.amazonaws.http.HttpResponseHandler

import cats.data.Xor
import cats.std.future._

import scala.concurrent.{ ExecutionContext, Future }

import httpclient._

object client {

  case class AkkaHttpClient(
      system: Option[ActorSystem] = None,
      mat: Option[ActorMaterializer] = None,
      ec: Option[ExecutionContext] = None
  ) extends HttpClient {

    implicit val s = system.getOrElse(ActorSystem("wookie"))
    implicit val m = mat.getOrElse(ActorMaterializer())
    implicit val e = ec.getOrElse(ExecutionContext.Implicits.global)

    def exec[A, B](request: Request[A])(implicit H: HttpResponseHandler[AmazonWebServiceResponse[B]]): Future[B] = {
      for {
        httpResponse <- http.sendRequest(http.createHttpRequest(request))
        marshalledResponse <- http.parseResponse(request.getServiceName, httpResponse)
      } yield marshalledResponse match {
        case Xor.Right(resp) => resp
        case Xor.Left(exc) => throw exc
      }
    }

  }

}
