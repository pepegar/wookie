package wookie

import com.amazonaws.http.HttpResponseHandler
import com.amazonaws.AmazonWebServiceResponse

object handler {
  trait Handler[A] {
    def responseHandler: HttpResponseHandler[AmazonWebServiceResponse[A]]
  }
}
