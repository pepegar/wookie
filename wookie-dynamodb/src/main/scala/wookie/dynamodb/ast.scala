package wookie
package dynamodb

import com.amazonaws.services.dynamodbv2.model._
import com.amazonaws.services.dynamodbv2.model.transform._
import com.amazonaws.transform.Marshaller
import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.http.JsonResponseHandler
import cats.free.Free

object ast {
  import marshaller.Marshallable

  sealed abstract class DynamoDBOp[A]
    extends Marshallable[A] with Product with Serializable {
    def req: AmazonWebServiceRequest
  }

  case class ListTables(req: ListTablesRequest)
    extends DynamoDBOp[ListTablesResult]
    with Marshallable[ListTablesResult] {
    def responseHandler = new JsonResponseHandler(ListTablesResultJsonUnmarshaller.getInstance())
  }
  case class Query(req: QueryRequest)
    extends DynamoDBOp[QueryResult]
    with Marshallable[QueryResult] {
    def responseHandler = new JsonResponseHandler(QueryResultJsonUnmarshaller.getInstance())
  }
  case class Scan(req: ScanRequest)
    extends DynamoDBOp[ScanResult]
    with Marshallable[ScanResult] {
    def responseHandler = new JsonResponseHandler(ScanResultJsonUnmarshaller.getInstance())
  }
  case class UpdateItem(req: UpdateItemRequest)
    extends DynamoDBOp[UpdateItemResult]
    with Marshallable[UpdateItemResult] {
    def responseHandler = new JsonResponseHandler(UpdateItemResultJsonUnmarshaller.getInstance())
  }
  case class PutItem(req: PutItemRequest)
    extends DynamoDBOp[PutItemResult]
    with Marshallable[PutItemResult] {
    def responseHandler = new JsonResponseHandler(PutItemResultJsonUnmarshaller.getInstance())
  }
  case class DescribeTable(req: DescribeTableRequest)
    extends DynamoDBOp[DescribeTableResult]
    with Marshallable[DescribeTableResult] {
    def responseHandler = new JsonResponseHandler(DescribeTableResultJsonUnmarshaller.getInstance())
  }
  case class CreateTable(req: CreateTableRequest)
    extends DynamoDBOp[CreateTableResult]
    with Marshallable[CreateTableResult] {
    def responseHandler = new JsonResponseHandler(CreateTableResultJsonUnmarshaller.getInstance())
  }
  case class UpdateTable(req: UpdateTableRequest)
    extends DynamoDBOp[UpdateTableResult]
    with Marshallable[UpdateTableResult] {
    def responseHandler = new JsonResponseHandler(UpdateTableResultJsonUnmarshaller.getInstance())
  }
  case class DeleteTable(req: DeleteTableRequest)
    extends DynamoDBOp[DeleteTableResult]
    with Marshallable[DeleteTableResult] {
    def responseHandler = new JsonResponseHandler(DeleteTableResultJsonUnmarshaller.getInstance())
  }
  case class GetItem(req: GetItemRequest)
    extends DynamoDBOp[GetItemResult]
    with Marshallable[GetItemResult] {
    def responseHandler = new JsonResponseHandler(GetItemResultJsonUnmarshaller.getInstance())
  }
  case class BatchWriteItem(req: BatchWriteItemRequest)
    extends DynamoDBOp[BatchWriteItemResult]
    with Marshallable[BatchWriteItemResult] {
    def responseHandler = new JsonResponseHandler(BatchWriteItemResultJsonUnmarshaller.getInstance())
  }
  case class BatchGetItem(req: BatchGetItemRequest)
    extends DynamoDBOp[BatchGetItemResult]
    with Marshallable[BatchGetItemResult] {
    def responseHandler = new JsonResponseHandler(BatchGetItemResultJsonUnmarshaller.getInstance())
  }
  case class DeleteItem(req: DeleteItemRequest)
    extends DynamoDBOp[DeleteItemResult]
    with Marshallable[DeleteItemResult] {
    def responseHandler = new JsonResponseHandler(DeleteItemResultJsonUnmarshaller.getInstance())
  }

  type DynamoDBMonad[A] = Free[DynamoDBOp, A]
}