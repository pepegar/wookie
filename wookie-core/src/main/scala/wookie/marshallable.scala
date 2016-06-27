package wookie

import com.amazonaws.http.HttpResponseHandler
import com.amazonaws.AmazonWebServiceResponse

object marshallable {
  trait Marshallable[A] {
    def responseHandler: HttpResponseHandler[AmazonWebServiceResponse[A]]
  }
}
