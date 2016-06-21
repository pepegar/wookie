package aws
package dynamodb

import cats.~>
import cats.data.{ Xor, Kleisli }
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.http.HttpResponseHandler
import com.amazonaws.AmazonWebServiceResponse
import com.amazonaws.Request
import com.amazonaws.transform.Marshaller
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object interpreter {
  import ast._
  import http._
  import DynamoDB._

  def futureInterpreter(endpoint: String)(
    implicit system: ActorSystem,
             mat: ActorMaterializer
  ) = new (DynamoDBOp ~> Result) {
    def apply[A](command: DynamoDBOp[A]): Result[A] = command match {
      case req @ ListTables(awsOp) => Kleisli { (marshaller: Marshaller[Request[A], A]) =>
        implicit val handler = req.responseHandler
        val request = marshaller.marshall(awsOp.asInstanceOf[A])
        send(request)
      }
      case req @ Query(awsOp) => Kleisli { (marshaller: Marshaller[Request[A], A]) =>
        implicit val handler = req.responseHandler
        val request = marshaller.marshall(awsOp.asInstanceOf[A])
        send(request)
      }
      case req @ Scan(awsOp) => Kleisli { (marshaller: Marshaller[Request[A], A]) =>
        implicit val handler = req.responseHandler
        val request = marshaller.marshall(awsOp.asInstanceOf[A])
        send(request)
      }
      case req @ UpdateItem(awsOp) => Kleisli { (marshaller: Marshaller[Request[A], A]) =>
        implicit val handler = req.responseHandler
        val request = marshaller.marshall(awsOp.asInstanceOf[A])
        send(request)
      }
      case req @ PutItem(awsOp) => Kleisli { (marshaller: Marshaller[Request[A], A]) =>
        implicit val handler = req.responseHandler
        val request = marshaller.marshall(awsOp.asInstanceOf[A])
        send(request)
      }
      case req @ DescribeTable(awsOp) => Kleisli { (marshaller: Marshaller[Request[A], A]) =>
        implicit val handler = req.responseHandler
        val request = marshaller.marshall(awsOp.asInstanceOf[A])
        send(request)
      }
      case req @ CreateTable(awsOp) => Kleisli { (marshaller: Marshaller[Request[A], A]) =>
        implicit val handler = req.responseHandler
        val request = marshaller.marshall(awsOp.asInstanceOf[A])
        send(request)
      }
      case req @ UpdateTable(awsOp) => Kleisli { (marshaller: Marshaller[Request[A], A]) =>
        implicit val handler = req.responseHandler
        val request = marshaller.marshall(awsOp.asInstanceOf[A])
        send(request)
      }
      case req @ DeleteTable(awsOp) => Kleisli { (marshaller: Marshaller[Request[A], A]) =>
        implicit val handler = req.responseHandler
        val request = marshaller.marshall(awsOp.asInstanceOf[A])
        send(request)
      }
      case req @ GetItem(awsOp) => Kleisli { (marshaller: Marshaller[Request[A], A]) =>
        implicit val handler = req.responseHandler
        val request = marshaller.marshall(awsOp.asInstanceOf[A])
        send(request)
      }
      case req @ BatchWriteItem(awsOp) => Kleisli { (marshaller: Marshaller[Request[A], A]) =>
        implicit val handler = req.responseHandler
        val request = marshaller.marshall(awsOp.asInstanceOf[A])
        send(request)
      }
      case req @ BatchGetItem(awsOp) => Kleisli { (marshaller: Marshaller[Request[A], A]) =>
        implicit val handler = req.responseHandler
        val request = marshaller.marshall(awsOp.asInstanceOf[A])
        send(request)
      }
      case req @ DeleteItem(awsOp) => Kleisli { (marshaller: Marshaller[Request[A], A]) =>
        implicit val handler = req.responseHandler
        val request = marshaller.marshall(awsOp.asInstanceOf[A])
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
