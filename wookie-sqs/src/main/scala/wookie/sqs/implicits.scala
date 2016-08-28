package wookie.sqs

import com.amazonaws.AmazonServiceException
import org.w3c.dom.Node
import com.amazonaws.http.{ DefaultErrorResponseHandler, StaxResponseHandler }
import com.amazonaws.services.sqs.model.transform._
import com.amazonaws.transform.{ StandardErrorUnmarshaller, Unmarshaller, VoidStaxUnmarshaller }

import collection.JavaConversions._

object implicits {
  // gently stolen from sclasen/akka-aws
  implicit val unitU = new StaxResponseHandler(new VoidStaxUnmarshaller[Unit]())
  implicit val addPermissionM = new AddPermissionRequestMarshaller()
  implicit val changeMessageVisibilityBatchM = new ChangeMessageVisibilityBatchRequestMarshaller()
  implicit val changeMessageVisibilityBatchU = new StaxResponseHandler(ChangeMessageVisibilityBatchResultStaxUnmarshaller.getInstance)
  implicit val createQueueM = new CreateQueueRequestMarshaller()
  implicit val createQueueU = new StaxResponseHandler(CreateQueueResultStaxUnmarshaller.getInstance)
  implicit val deleteMessageBatchM = new DeleteMessageBatchRequestMarshaller()
  implicit val deleteMessageBatchU = new StaxResponseHandler(DeleteMessageBatchResultStaxUnmarshaller.getInstance)
  implicit val deleteMessageM = new DeleteMessageRequestMarshaller()
  implicit val deleteQueueM = new DeleteQueueRequestMarshaller()
  implicit val getQueueAttributesM = new GetQueueAttributesRequestMarshaller()
  implicit val getQueueAttributesU = new StaxResponseHandler(GetQueueAttributesResultStaxUnmarshaller.getInstance)
  implicit val getQueueUrlM = new GetQueueUrlRequestMarshaller()
  implicit val getQueueUrlU = new StaxResponseHandler(GetQueueUrlResultStaxUnmarshaller.getInstance)
  implicit val listDeadLetterSourceQueuesM = new ListDeadLetterSourceQueuesRequestMarshaller()
  implicit val listDeadLetterSourceQueuesU = new StaxResponseHandler(ListDeadLetterSourceQueuesResultStaxUnmarshaller.getInstance)
  implicit val listQueuesM = new ListQueuesRequestMarshaller()
  implicit val listQueuesU = new StaxResponseHandler(ListQueuesResultStaxUnmarshaller.getInstance)
  implicit val receiveMessageM = new ReceiveMessageRequestMarshaller()
  implicit val receiveMessageU = new StaxResponseHandler(ReceiveMessageResultStaxUnmarshaller.getInstance)
  implicit val removePermissionM = new RemovePermissionRequestMarshaller()
  implicit val sendMessageBatchM = new SendMessageBatchRequestMarshaller()
  implicit val sendMessageBatchU = new StaxResponseHandler(SendMessageBatchResultStaxUnmarshaller.getInstance)
  implicit val sendMessageM = new SendMessageRequestMarshaller()
  implicit val sendMessageU = new StaxResponseHandler(SendMessageResultStaxUnmarshaller.getInstance)
  implicit val setQueueAttributesM = new SetQueueAttributesRequestMarshaller()

  val sqsExceptionUnmarshallers = List[Unmarshaller[AmazonServiceException, Node]](
    new BatchEntryIdsNotDistinctExceptionUnmarshaller(),
    new BatchRequestTooLongExceptionUnmarshaller(),
    new EmptyBatchRequestExceptionUnmarshaller(),
    new InvalidAttributeNameExceptionUnmarshaller(),
    new InvalidBatchEntryIdExceptionUnmarshaller(),
    new InvalidIdFormatExceptionUnmarshaller(),
    new InvalidMessageContentsExceptionUnmarshaller(),
    new MessageNotInflightExceptionUnmarshaller(),
    new OverLimitExceptionUnmarshaller(),
    new QueueDeletedRecentlyExceptionUnmarshaller(),
    new QueueDoesNotExistExceptionUnmarshaller(),
    new QueueNameExistsExceptionUnmarshaller(),
    new ReceiptHandleIsInvalidExceptionUnmarshaller(),
    new TooManyEntriesInBatchRequestExceptionUnmarshaller(),
    new StandardErrorUnmarshaller()
  )

  implicit val errorResponseHandler = new DefaultErrorResponseHandler(sqsExceptionUnmarshallers)
}