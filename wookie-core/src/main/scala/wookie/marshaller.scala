package wookie

import com.amazonaws.auth._
import com.amazonaws.{ AmazonWebServiceRequest, Request, AmazonWebServiceResponse }
import com.amazonaws.http.{ HttpResponseHandler, JsonErrorResponseHandler, JsonResponseHandler }

object marshaller {
  trait Marshallable[A] {
    def responseHandler: HttpResponseHandler[AmazonWebServiceResponse[A]]
  }

  trait Signer[A] {
    def endpoint: String

    def credentials: AWSCredentials

    lazy val signer = {
      val s = new AWS4Signer(true)
      s.setServiceName(endpoint)
      s
    }

    def sign[T](request: Request[T]): Request[T] = {
      // TODO: immutabilize this...
      signer.sign(request, credentials)
      request
    }
  }

  object Signer {

    def apply[A](e: String, c: AWSCredentials): Signer[A] = new Signer[A] {
      def endpoint = e
      def credentials = c
    }

  }
}
