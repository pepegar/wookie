package wookie
package dynamodb

import com.amazonaws.services.dynamodbv2.model._
import com.amazonaws.services.dynamodbv2.model.transform._
import com.amazonaws.transform.Marshaller
import com.amazonaws.{ AmazonWebServiceRequest, Request }
import com.amazonaws.http.JsonResponseHandler
import cats.free.Free

object ast {

  import handler._

  sealed abstract class DynamoDBOp[A]
      extends Handler[A]
      with Product with Serializable {
    def req: Request[_]
  }

  type MM[A] = Marshaller[Request[A], A]

  case class ListTables(value: ListTablesRequest)(implicit M: MM[ListTablesRequest])
      extends DynamoDBOp[ListTablesResult] {
    def responseHandler = new JsonResponseHandler(ListTablesResultJsonUnmarshaller.getInstance())
    def req = M.marshall(value)
  }
  case class Query(value: QueryRequest)(implicit M: MM[QueryRequest])
      extends DynamoDBOp[QueryResult] {
    def responseHandler = new JsonResponseHandler(QueryResultJsonUnmarshaller.getInstance())
    def req = M.marshall(value)
  }
  case class Scan(value: ScanRequest)(implicit M: MM[ScanRequest])
      extends DynamoDBOp[ScanResult] {
    def responseHandler = new JsonResponseHandler(ScanResultJsonUnmarshaller.getInstance())
    def req = M.marshall(value)
  }
  case class UpdateItem(value: UpdateItemRequest)(implicit M: MM[UpdateItemRequest])
      extends DynamoDBOp[UpdateItemResult] {
    def responseHandler = new JsonResponseHandler(UpdateItemResultJsonUnmarshaller.getInstance())
    def req = M.marshall(value)
  }
  case class PutItem(value: PutItemRequest)(implicit M: MM[PutItemRequest])
      extends DynamoDBOp[PutItemResult] {
    def responseHandler = new JsonResponseHandler(PutItemResultJsonUnmarshaller.getInstance())
    def req = M.marshall(value)
  }
  case class DescribeTable(value: DescribeTableRequest)(implicit M: MM[DescribeTableRequest])
      extends DynamoDBOp[DescribeTableResult] {
    def responseHandler = new JsonResponseHandler(DescribeTableResultJsonUnmarshaller.getInstance())
    def req = M.marshall(value)
  }
  case class CreateTable(value: CreateTableRequest)(implicit M: MM[CreateTableRequest])
      extends DynamoDBOp[CreateTableResult] {
    def responseHandler = new JsonResponseHandler(CreateTableResultJsonUnmarshaller.getInstance())
    def req = M.marshall(value)
  }
  case class UpdateTable(value: UpdateTableRequest)(implicit M: MM[UpdateTableRequest])
      extends DynamoDBOp[UpdateTableResult] {
    def responseHandler = new JsonResponseHandler(UpdateTableResultJsonUnmarshaller.getInstance())
    def req = M.marshall(value)
  }
  case class DeleteTable(value: DeleteTableRequest)(implicit M: MM[DeleteTableRequest])
      extends DynamoDBOp[DeleteTableResult] {
    def responseHandler = new JsonResponseHandler(DeleteTableResultJsonUnmarshaller.getInstance())
    def req = M.marshall(value)
  }
  case class GetItem(value: GetItemRequest)(implicit M: MM[GetItemRequest])
      extends DynamoDBOp[GetItemResult] {
    def responseHandler = new JsonResponseHandler(GetItemResultJsonUnmarshaller.getInstance())
    def req = M.marshall(value)
  }
  case class BatchWriteItem(value: BatchWriteItemRequest)(implicit M: MM[BatchWriteItemRequest])
      extends DynamoDBOp[BatchWriteItemResult] {
    def responseHandler = new JsonResponseHandler(BatchWriteItemResultJsonUnmarshaller.getInstance())
    def req = M.marshall(value)
  }
  case class BatchGetItem(value: BatchGetItemRequest)(implicit M: MM[BatchGetItemRequest])
      extends DynamoDBOp[BatchGetItemResult] {
    def responseHandler = new JsonResponseHandler(BatchGetItemResultJsonUnmarshaller.getInstance())
    def req = M.marshall(value)
  }
  case class DeleteItem(value: DeleteItemRequest)(implicit M: MM[DeleteItemRequest])
      extends DynamoDBOp[DeleteItemResult] {
    def responseHandler = new JsonResponseHandler(DeleteItemResultJsonUnmarshaller.getInstance())
    def req = M.marshall(value)
  }

  type DynamoDBMonad[A] = Free[DynamoDBOp, A]
}
