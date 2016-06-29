package wookie

import com.amazonaws.{ Request, AmazonWebServiceResponse }
import com.amazonaws.http.HttpResponseHandler

import cats.MonadError

import scala.concurrent.Future

object httpclient {

  trait HttpClient {

    def exec[A, B](request: Request[A])(implicit H: HttpResponseHandler[AmazonWebServiceResponse[B]]): Future[B]

  }

}