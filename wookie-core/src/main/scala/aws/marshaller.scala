package wookie

import com.amazonaws.auth._
import com.amazonaws.Request
import com.amazonaws.{ AmazonWebServiceRequest, Request }
import com.amazonaws.transform.Marshaller

object marshaller {
  trait SignMarshaller[A] {
    def marshaller:  Marshaller[Request[A], AmazonWebServiceRequest]

    def endpoint: String

    def credentials: AWSCredentials

    lazy val signer = {
      val s = new AWS4Signer(true)
      s.setServiceName(endpoint)
      s
    }

    def marshallAndSign(t: AmazonWebServiceRequest, credentials: AWSCredentials): Request[A] =
      sign(marshaller.marshall(t), credentials)

    private[this] def sign[T](request: Request[T], credentials: AWSCredentials): Request[T] = {
      // TODO: immutabilize this...
      signer.sign(request, credentials)
      request
    }
  }

  object SignMarshaller {

    def apply[A]
      (e: String, c: AWSCredentials)
      (implicit M: Marshaller[Request[A], AmazonWebServiceRequest]
    ): SignMarshaller[A] = new SignMarshaller[A] {
      def endpoint = e
      def credentials = c
      def marshaller = M
    }

  }
}
