package wookie.sqs

import java.util.{ List, Map }

import cats.free.Free
import com.amazonaws.{ AmazonWebServiceResponse, Request }
import com.amazonaws.http.HttpResponseHandler
import com.amazonaws.services.sqs.model._
import com.amazonaws.transform.Marshaller
import wookie.handler
import wookie.sqs.implicits._

object algebra {

  import handler._

  type SQSIO[A] = Free[SQSOp, A]

  sealed trait SQSOp[A]
      extends Handler[A]
      with Product with Serializable {
    def marshalledReq: Request[_]
  }

  object SQSOp {

    case class AddPermission()(implicit M: Marshaller[Request[AddPermissionRequest], AddPermissionRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[Unit]])
        extends SQSOp[Unit] {
      def amzReq = new AddPermissionRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class AddPermission1(a: String, b: String, c: List[String], d: List[String])(implicit M: Marshaller[Request[AddPermissionRequest], AddPermissionRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[Unit]])
        extends SQSOp[Unit] {
      def amzReq = new AddPermissionRequest(a, b, c, d)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class ChangeMessageVisibilityBatch()(implicit M: Marshaller[Request[ChangeMessageVisibilityBatchRequest], ChangeMessageVisibilityBatchRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[ChangeMessageVisibilityBatchResult]])
        extends SQSOp[ChangeMessageVisibilityBatchResult] {
      def amzReq = new ChangeMessageVisibilityBatchRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class ChangeMessageVisibilityBatch1(a: String, b: List[ChangeMessageVisibilityBatchRequestEntry])(implicit M: Marshaller[Request[ChangeMessageVisibilityBatchRequest], ChangeMessageVisibilityBatchRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[ChangeMessageVisibilityBatchResult]])
        extends SQSOp[ChangeMessageVisibilityBatchResult] {
      def amzReq = new ChangeMessageVisibilityBatchRequest(a, b)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class CreateQueue()(implicit M: Marshaller[Request[CreateQueueRequest], CreateQueueRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[CreateQueueResult]])
        extends SQSOp[CreateQueueResult] {
      def amzReq = new CreateQueueRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class CreateQueue1(a: String)(implicit M: Marshaller[Request[CreateQueueRequest], CreateQueueRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[CreateQueueResult]])
        extends SQSOp[CreateQueueResult] {
      def amzReq = new CreateQueueRequest(a)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class DeleteMessageBatch(a: String, b: List[DeleteMessageBatchRequestEntry])(implicit M: Marshaller[Request[DeleteMessageBatchRequest], DeleteMessageBatchRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[DeleteMessageBatchResult]])
        extends SQSOp[DeleteMessageBatchResult] {
      def amzReq = new DeleteMessageBatchRequest(a, b)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class DeleteMessageBatch1(a: String)(implicit M: Marshaller[Request[DeleteMessageBatchRequest], DeleteMessageBatchRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[DeleteMessageBatchResult]])
        extends SQSOp[DeleteMessageBatchResult] {
      def amzReq = new DeleteMessageBatchRequest(a)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class DeleteMessageBatch2()(implicit M: Marshaller[Request[DeleteMessageBatchRequest], DeleteMessageBatchRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[DeleteMessageBatchResult]])
        extends SQSOp[DeleteMessageBatchResult] {
      def amzReq = new DeleteMessageBatchRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class DeleteMessage()(implicit M: Marshaller[Request[DeleteMessageRequest], DeleteMessageRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[Unit]])
        extends SQSOp[Unit] {
      def amzReq = new DeleteMessageRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class DeleteMessage1(a: String, b: String)(implicit M: Marshaller[Request[DeleteMessageRequest], DeleteMessageRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[Unit]])
        extends SQSOp[Unit] {
      def amzReq = new DeleteMessageRequest(a, b)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class DeleteQueue()(implicit M: Marshaller[Request[DeleteQueueRequest], DeleteQueueRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[Unit]])
        extends SQSOp[Unit] {
      def amzReq = new DeleteQueueRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class DeleteQueue1(a: String)(implicit M: Marshaller[Request[DeleteQueueRequest], DeleteQueueRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[Unit]])
        extends SQSOp[Unit] {
      def amzReq = new DeleteQueueRequest(a)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class GetQueueAttributes(a: String, b: List[String])(implicit M: Marshaller[Request[GetQueueAttributesRequest], GetQueueAttributesRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[GetQueueAttributesResult]])
        extends SQSOp[GetQueueAttributesResult] {
      def amzReq = new GetQueueAttributesRequest(a, b)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class GetQueueAttributes1(a: String)(implicit M: Marshaller[Request[GetQueueAttributesRequest], GetQueueAttributesRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[GetQueueAttributesResult]])
        extends SQSOp[GetQueueAttributesResult] {
      def amzReq = new GetQueueAttributesRequest(a)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class GetQueueAttributes2()(implicit M: Marshaller[Request[GetQueueAttributesRequest], GetQueueAttributesRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[GetQueueAttributesResult]])
        extends SQSOp[GetQueueAttributesResult] {
      def amzReq = new GetQueueAttributesRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class GetQueueUrl()(implicit M: Marshaller[Request[GetQueueUrlRequest], GetQueueUrlRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[GetQueueUrlResult]])
        extends SQSOp[GetQueueUrlResult] {
      def amzReq = new GetQueueUrlRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class GetQueueUrl1(a: String)(implicit M: Marshaller[Request[GetQueueUrlRequest], GetQueueUrlRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[GetQueueUrlResult]])
        extends SQSOp[GetQueueUrlResult] {
      def amzReq = new GetQueueUrlRequest(a)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class ListDeadLetterSourceQueues()(implicit M: Marshaller[Request[ListDeadLetterSourceQueuesRequest], ListDeadLetterSourceQueuesRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[ListDeadLetterSourceQueuesResult]])
        extends SQSOp[ListDeadLetterSourceQueuesResult] {
      def amzReq = new ListDeadLetterSourceQueuesRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class ListQueues()(implicit M: Marshaller[Request[ListQueuesRequest], ListQueuesRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[ListQueuesResult]])
        extends SQSOp[ListQueuesResult] {
      def amzReq = new ListQueuesRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class ListQueues1(a: String)(implicit M: Marshaller[Request[ListQueuesRequest], ListQueuesRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[ListQueuesResult]])
        extends SQSOp[ListQueuesResult] {
      def amzReq = new ListQueuesRequest(a)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class ReceiveMessage()(implicit M: Marshaller[Request[ReceiveMessageRequest], ReceiveMessageRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[ReceiveMessageResult]])
        extends SQSOp[ReceiveMessageResult] {
      def amzReq = new ReceiveMessageRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class ReceiveMessage1(a: String)(implicit M: Marshaller[Request[ReceiveMessageRequest], ReceiveMessageRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[ReceiveMessageResult]])
        extends SQSOp[ReceiveMessageResult] {
      def amzReq = new ReceiveMessageRequest(a)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class SendMessageBatch(a: String, b: List[SendMessageBatchRequestEntry])(implicit M: Marshaller[Request[SendMessageBatchRequest], SendMessageBatchRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[SendMessageBatchResult]])
        extends SQSOp[SendMessageBatchResult] {
      def amzReq = new SendMessageBatchRequest(a, b)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class SendMessageBatch1(a: String)(implicit M: Marshaller[Request[SendMessageBatchRequest], SendMessageBatchRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[SendMessageBatchResult]])
        extends SQSOp[SendMessageBatchResult] {
      def amzReq = new SendMessageBatchRequest(a)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class SendMessageBatch2()(implicit M: Marshaller[Request[SendMessageBatchRequest], SendMessageBatchRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[SendMessageBatchResult]])
        extends SQSOp[SendMessageBatchResult] {
      def amzReq = new SendMessageBatchRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class SendMessage()(implicit M: Marshaller[Request[SendMessageRequest], SendMessageRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[SendMessageResult]])
        extends SQSOp[SendMessageResult] {
      def amzReq = new SendMessageRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class SendMessage1(a: String, b: String)(implicit M: Marshaller[Request[SendMessageRequest], SendMessageRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[SendMessageResult]])
        extends SQSOp[SendMessageResult] {
      def amzReq = new SendMessageRequest(a, b)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class SetQueueAttributes()(implicit M: Marshaller[Request[SetQueueAttributesRequest], SetQueueAttributesRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[Unit]])
        extends SQSOp[Unit] {
      def amzReq = new SetQueueAttributesRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class SetQueueAttributes1(a: String, b: Map[String, String])(implicit M: Marshaller[Request[SetQueueAttributesRequest], SetQueueAttributesRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[Unit]])
        extends SQSOp[Unit] {
      def amzReq = new SetQueueAttributesRequest(a, b)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }

  }

  object ops {

    import SQSOp._

    trait SQSOps[P[_]] {
      def addPermissionRequest: P[Unit]
      def addPermissionRequest(a: String, b: String, c: List[String], d: List[String]): P[Unit]
      def changeMessageVisibilityBatchRequest: P[ChangeMessageVisibilityBatchResult]
      def changeMessageVisibilityBatchRequest(a: String, b: List[ChangeMessageVisibilityBatchRequestEntry]): P[ChangeMessageVisibilityBatchResult]
      def createQueueRequest: P[CreateQueueResult]
      def createQueueRequest(a: String): P[CreateQueueResult]
      def deleteMessageBatchRequest(a: String, b: List[DeleteMessageBatchRequestEntry]): P[DeleteMessageBatchResult]
      def deleteMessageBatchRequest(a: String): P[DeleteMessageBatchResult]
      def deleteMessageBatchRequest: P[DeleteMessageBatchResult]
      def deleteMessageRequest: P[Unit]
      def deleteMessageRequest(a: String, b: String): P[Unit]
      def deleteQueueRequest: P[Unit]
      def deleteQueueRequest(a: String): P[Unit]
      def getQueueAttributesRequest(a: String, b: List[String]): P[GetQueueAttributesResult]
      def getQueueAttributesRequest(a: String): P[GetQueueAttributesResult]
      def getQueueAttributesRequest: P[GetQueueAttributesResult]
      def getQueueUrlRequest: P[GetQueueUrlResult]
      def getQueueUrlRequest(a: String): P[GetQueueUrlResult]
      def listDeadLetterSourceQueuesRequest: P[ListDeadLetterSourceQueuesResult]
      def listQueuesRequest: P[ListQueuesResult]
      def listQueuesRequest(a: String): P[ListQueuesResult]
      def receiveMessageRequest: P[ReceiveMessageResult]
      def receiveMessageRequest(a: String): P[ReceiveMessageResult]
      def sendMessageBatchRequest(a: String, b: List[SendMessageBatchRequestEntry]): P[SendMessageBatchResult]
      def sendMessageBatchRequest(a: String): P[SendMessageBatchResult]
      def sendMessageBatchRequest: P[SendMessageBatchResult]
      def sendMessageRequest: P[SendMessageResult]
      def sendMessageRequest(a: String, b: String): P[SendMessageResult]
      def setQueueAttributesRequest: P[Unit]
      def setQueueAttributesRequest(a: String, b: Map[String, String]): P[Unit]

    }

    object SQSOps extends SQSOps[SQSIO] {
      def addPermissionRequest: SQSIO[Unit] =
        Free.liftF(AddPermission())
      def addPermissionRequest(a: String, b: String, c: List[String], d: List[String]): SQSIO[Unit] =
        Free.liftF(AddPermission1(a, b, c, d))
      def changeMessageVisibilityBatchRequest: SQSIO[ChangeMessageVisibilityBatchResult] =
        Free.liftF(ChangeMessageVisibilityBatch())
      def changeMessageVisibilityBatchRequest(a: String, b: List[ChangeMessageVisibilityBatchRequestEntry]): SQSIO[ChangeMessageVisibilityBatchResult] =
        Free.liftF(ChangeMessageVisibilityBatch1(a, b))
      def createQueueRequest: SQSIO[CreateQueueResult] =
        Free.liftF(CreateQueue())
      def createQueueRequest(a: String): SQSIO[CreateQueueResult] =
        Free.liftF(CreateQueue1(a))
      def deleteMessageBatchRequest(a: String, b: List[DeleteMessageBatchRequestEntry]): SQSIO[DeleteMessageBatchResult] =
        Free.liftF(DeleteMessageBatch(a, b))
      def deleteMessageBatchRequest(a: String): SQSIO[DeleteMessageBatchResult] =
        Free.liftF(DeleteMessageBatch1(a))
      def deleteMessageBatchRequest: SQSIO[DeleteMessageBatchResult] =
        Free.liftF(DeleteMessageBatch2())
      def deleteMessageRequest: SQSIO[Unit] =
        Free.liftF(DeleteMessage())
      def deleteMessageRequest(a: String, b: String): SQSIO[Unit] =
        Free.liftF(DeleteMessage1(a, b))
      def deleteQueueRequest: SQSIO[Unit] =
        Free.liftF(DeleteQueue())
      def deleteQueueRequest(a: String): SQSIO[Unit] =
        Free.liftF(DeleteQueue1(a))
      def getQueueAttributesRequest(a: String, b: List[String]): SQSIO[GetQueueAttributesResult] =
        Free.liftF(GetQueueAttributes(a, b))
      def getQueueAttributesRequest(a: String): SQSIO[GetQueueAttributesResult] =
        Free.liftF(GetQueueAttributes1(a))
      def getQueueAttributesRequest: SQSIO[GetQueueAttributesResult] =
        Free.liftF(GetQueueAttributes2())
      def getQueueUrlRequest: SQSIO[GetQueueUrlResult] =
        Free.liftF(GetQueueUrl())
      def getQueueUrlRequest(a: String): SQSIO[GetQueueUrlResult] =
        Free.liftF(GetQueueUrl1(a))
      def listDeadLetterSourceQueuesRequest: SQSIO[ListDeadLetterSourceQueuesResult] =
        Free.liftF(ListDeadLetterSourceQueues())
      def listQueuesRequest: SQSIO[ListQueuesResult] =
        Free.liftF(ListQueues())
      def listQueuesRequest(a: String): SQSIO[ListQueuesResult] =
        Free.liftF(ListQueues1(a))
      def receiveMessageRequest: SQSIO[ReceiveMessageResult] =
        Free.liftF(ReceiveMessage())
      def receiveMessageRequest(a: String): SQSIO[ReceiveMessageResult] =
        Free.liftF(ReceiveMessage1(a))
      def sendMessageBatchRequest(a: String, b: List[SendMessageBatchRequestEntry]): SQSIO[SendMessageBatchResult] =
        Free.liftF(SendMessageBatch(a, b))
      def sendMessageBatchRequest(a: String): SQSIO[SendMessageBatchResult] =
        Free.liftF(SendMessageBatch1(a))
      def sendMessageBatchRequest: SQSIO[SendMessageBatchResult] =
        Free.liftF(SendMessageBatch2())
      def sendMessageRequest: SQSIO[SendMessageResult] =
        Free.liftF(SendMessage())
      def sendMessageRequest(a: String, b: String): SQSIO[SendMessageResult] =
        Free.liftF(SendMessage1(a, b))
      def setQueueAttributesRequest: SQSIO[Unit] =
        Free.liftF(SetQueueAttributes())
      def setQueueAttributesRequest(a: String, b: Map[String, String]): SQSIO[Unit] =
        Free.liftF(SetQueueAttributes1(a, b))

    }
  }

}
