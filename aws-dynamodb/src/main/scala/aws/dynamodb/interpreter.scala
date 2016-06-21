package aws
package dynamodb

import cats.~>
import cats.data.Xor
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.http.HttpResponseHandler
import com.amazonaws.AmazonWebServiceResponse
import com.amazonaws.Request
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object interpreter {
  import ast._
  import http._

  def futureInterpreter(endpoint: String)(
    implicit system: ActorSystem,
             mat: ActorMaterializer
  ) = new (DynamoDBOp ~> Future) {
    def apply[A](command: DynamoDBOp[A]): Future[A] = command match {
      case req @ ListTables(awsOp) => {
        implicit val handler = req.responseHandler
        val request = req.marshaller.marshall(awsOp)
        send(request)
      }
      case req @ Query(awsOp) => {
        implicit val handler = req.responseHandler
        val request = req.marshaller.marshall(awsOp)
        send(request)
      }
      case req @ Scan(awsOp) => {
        implicit val handler = req.responseHandler
        val request = req.marshaller.marshall(awsOp)
        send(request)
      }
      case req @ UpdateItem(awsOp) => {
        implicit val handler = req.responseHandler
        val request = req.marshaller.marshall(awsOp)
        send(request)
      }
      case req @ PutItem(awsOp) => {
        implicit val handler = req.responseHandler
        val request = req.marshaller.marshall(awsOp)
        send(request)
      }
      case req @ DescribeTable(awsOp) => {
        implicit val handler = req.responseHandler
        val request = req.marshaller.marshall(awsOp)
        send(request)
      }
      case req @ CreateTable(awsOp) => {
        implicit val handler = req.responseHandler
        val request = req.marshaller.marshall(awsOp)
        send(request)
      }
      case req @ UpdateTable(awsOp) => {
        implicit val handler = req.responseHandler
        val request = req.marshaller.marshall(awsOp)
        send(request)
      }
      case req @ DeleteTable(awsOp) => {
        implicit val handler = req.responseHandler
        val request = req.marshaller.marshall(awsOp)
        send(request)
      }
      case req @ GetItem(awsOp) => {
        implicit val handler = req.responseHandler
        val request = req.marshaller.marshall(awsOp)
        send(request)
      }
      case req @ BatchWriteItem(awsOp) => {
        implicit val handler = req.responseHandler
        val request = req.marshaller.marshall(awsOp)
        send(request)
      }
      case req @ BatchGetItem(awsOp) => {
        implicit val handler = req.responseHandler
        val request = req.marshaller.marshall(awsOp)
        send(request)
      }
      case req @ DeleteItem(awsOp) => {
        implicit val handler = req.responseHandler
        val request = req.marshaller.marshall(awsOp)
        send(request)
      }
    }

    def send[T, R](request: Request[T])(
      implicit handler: HttpResponseHandler[AmazonWebServiceResponse[R]]
      ): Future[R] = {
      for {
        httpResponse <- sendRequest(createHttpRequest(request))
        marshalledResponse <- parseResponse(endpoint, httpResponse)
      } yield marshalledResponse match {
        case Xor.Right(resp) => resp
        case Xor.Left(exc) => throw exc
      }
    }
  }

}
