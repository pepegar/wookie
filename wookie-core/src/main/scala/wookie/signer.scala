package wookie

import com.amazonaws.Request
import com.amazonaws.auth._

object signer {

  trait Signer {
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

    def apply(e: String, n: String, c: AWSCredentials): Signer = new Signer {
      def endpoint = e
      def serviceName = n
      def credentials = c
    }

  }
}
