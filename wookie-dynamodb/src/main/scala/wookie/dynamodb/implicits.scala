package wookie
package dynamodb

import collection.JavaConversions._

import com.amazonaws.http.{ JsonErrorResponseHandler, JsonResponseHandler }
import com.amazonaws.services.dynamodbv2.model.transform._
import com.amazonaws.transform.JsonErrorUnmarshaller

object implicits {
  // gently stolen from sclasen/akka-aws
  implicit val batchWriteM = new BatchWriteItemRequestMarshaller()
  implicit val batchWriteU = new JsonResponseHandler(BatchWriteItemResultJsonUnmarshaller.getInstance())
  implicit val putItemM = new PutItemRequestMarshaller()
  implicit val putItemU = new JsonResponseHandler(PutItemResultJsonUnmarshaller.getInstance())
  implicit val delItemM = new DeleteItemRequestMarshaller()
  implicit val delItemU = new JsonResponseHandler(DeleteItemResultJsonUnmarshaller.getInstance())
  implicit val batchGetM = new BatchGetItemRequestMarshaller()
  implicit val batchGetU = new JsonResponseHandler(BatchGetItemResultJsonUnmarshaller.getInstance())
  implicit val listM = new ListTablesRequestMarshaller()
  implicit val listU = new JsonResponseHandler(ListTablesResultJsonUnmarshaller.getInstance())
  implicit val qM = new QueryRequestMarshaller()
  implicit val qU = new JsonResponseHandler(QueryResultJsonUnmarshaller.getInstance())
  implicit val uM = new UpdateItemRequestMarshaller()
  implicit val uU = new JsonResponseHandler(UpdateItemResultJsonUnmarshaller.getInstance())
  implicit val dM = new DescribeTableRequestMarshaller()
  implicit val dU = new JsonResponseHandler(DescribeTableResultJsonUnmarshaller.getInstance())
  implicit val sM = new ScanRequestMarshaller()
  implicit val sU = new JsonResponseHandler(ScanResultJsonUnmarshaller.getInstance())
  implicit val cM = new CreateTableRequestMarshaller()
  implicit val cU = new JsonResponseHandler(CreateTableResultJsonUnmarshaller.getInstance())
  implicit val upM = new UpdateTableRequestMarshaller()
  implicit val upU = new JsonResponseHandler(UpdateTableResultJsonUnmarshaller.getInstance())
  implicit val deM = new DeleteTableRequestMarshaller()
  implicit val deU = new JsonResponseHandler(DeleteTableResultJsonUnmarshaller.getInstance())
  implicit val getM = new GetItemRequestMarshaller()
  implicit val getU = new JsonResponseHandler(GetItemResultJsonUnmarshaller.getInstance())

  implicit val errorResponseHandler = new JsonErrorResponseHandler(dynamoExceptionUnmarshallers)

  val dynamoExceptionUnmarshallers = List[JsonErrorUnmarshaller](
    new LimitExceededExceptionUnmarshaller(),
    new InternalServerErrorExceptionUnmarshaller(),
    new ProvisionedThroughputExceededExceptionUnmarshaller(),
    new ResourceInUseExceptionUnmarshaller(),
    new ConditionalCheckFailedExceptionUnmarshaller(),
    new ResourceNotFoundExceptionUnmarshaller(),
    new JsonErrorUnmarshaller()
  )
}
