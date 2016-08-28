package wookie

import com.amazonaws.AmazonWebServiceResponse
import com.amazonaws.http.HttpResponseHandler

object handler {
  trait Handler[A] {
    def responseHandler: HttpResponseHandler[AmazonWebServiceResponse[A]]
  }
}
