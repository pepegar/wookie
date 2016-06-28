package wookie

import com.amazonaws.auth._
import com.amazonaws.Request

object signer {

  trait Signer[A] {
    def endpoint: String

    def serviceName: String

    def credentials: AWSCredentials

    lazy val signer = {
      val s = new AWS4Signer(true)
      s.setServiceName(serviceName)
      s
    }

    def sign[T](request: Request[T]): Request[T] = {
      request.setResourcePath(endpoint)
      request.setEndpoint(new java.net.URI(endpoint))
      signer.sign(request, credentials)
      request
    }
  }

  object Signer {

    def apply[A](e: String, n: String, c: AWSCredentials): Signer[A] = new Signer[A] {
      def endpoint = e
      def serviceName = n
      def credentials = c
    }

  }
}
