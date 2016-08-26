package wookie

import com.amazonaws.http.HttpResponseHandler
import com.amazonaws.{ AmazonServiceException, AmazonWebServiceResponse, Request }

import scala.concurrent.Future

object httpclient {

  trait HttpClient {

    def exec[A, B](request: Request[A])(implicit H: HttpResponseHandler[AmazonWebServiceResponse[B]]): Future[B]

    def errorResponseHandler: HttpResponseHandler[AmazonServiceException]

  }

}
