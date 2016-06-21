package aws
package dynamodb

import com.amazonaws.services.dynamodbv2.model._
import com.amazonaws.services.dynamodbv2.model.transform._
import com.amazonaws.transform.Marshaller
import com.amazonaws.{ AmazonWebServiceRequest, AmazonWebServiceResponse, Request }
import com.amazonaws.http.{ HttpResponseHandler, JsonErrorResponseHandler, JsonResponseHandler }
import cats.free.Free

object ast {
  trait Marshallable[A, B] {
    def responseHandler: HttpResponseHandler[AmazonWebServiceResponse[B]]
  }

  sealed trait DynamoDBOp[T] {
    def req: AmazonWebServiceRequest
  }
  case class ListTables(req: ListTablesRequest)
    extends DynamoDBOp[ListTablesResult]
    with Marshallable[ListTablesRequest, ListTablesResult] {
      def responseHandler = new JsonResponseHandler(ListTablesResultJsonUnmarshaller.getInstance())
    }
  case class Query(req: QueryRequest)
    extends DynamoDBOp[QueryResult]
    with Marshallable[QueryRequest, QueryResult] {
      def responseHandler = new JsonResponseHandler(QueryResultJsonUnmarshaller.getInstance())
    }
  case class Scan(req: ScanRequest)
    extends DynamoDBOp[ScanResult]
    with Marshallable[ScanRequest, ScanResult] {
      def responseHandler = new JsonResponseHandler(ScanResultJsonUnmarshaller.getInstance())
    }
  case class UpdateItem(req: UpdateItemRequest)
    extends DynamoDBOp[UpdateItemResult]
    with Marshallable[UpdateItemRequest, UpdateItemResult] {
      def responseHandler = new JsonResponseHandler(UpdateItemResultJsonUnmarshaller.getInstance())
    }
  case class PutItem(req: PutItemRequest)
    extends DynamoDBOp[PutItemResult]
    with Marshallable[PutItemRequest, PutItemResult] {
      def responseHandler = new JsonResponseHandler(PutItemResultJsonUnmarshaller.getInstance())
    }
  case class DescribeTable(req: DescribeTableRequest)
    extends DynamoDBOp[DescribeTableResult]
    with Marshallable[DescribeTableRequest, DescribeTableResult] {
      def responseHandler = new JsonResponseHandler(DescribeTableResultJsonUnmarshaller.getInstance())
    }
  case class CreateTable(req: CreateTableRequest)
    extends DynamoDBOp[CreateTableResult]
    with Marshallable[CreateTableRequest, CreateTableResult] {
      def responseHandler = new JsonResponseHandler(CreateTableResultJsonUnmarshaller.getInstance())
    }
  case class UpdateTable(req: UpdateTableRequest)
    extends DynamoDBOp[UpdateTableResult]
    with Marshallable[UpdateTableRequest, UpdateTableResult] {
      def responseHandler = new JsonResponseHandler(UpdateTableResultJsonUnmarshaller.getInstance())
    }
  case class DeleteTable(req: DeleteTableRequest)
    extends DynamoDBOp[DeleteTableResult]
    with Marshallable[DeleteTableRequest, DeleteTableResult] {
      def responseHandler = new JsonResponseHandler(DeleteTableResultJsonUnmarshaller.getInstance())
    }
  case class GetItem(req: GetItemRequest)
    extends DynamoDBOp[GetItemResult]
    with Marshallable[GetItemRequest, GetItemResult] {
      def responseHandler = new JsonResponseHandler(GetItemResultJsonUnmarshaller.getInstance())
    }
  case class BatchWriteItem(req: BatchWriteItemRequest)
    extends DynamoDBOp[BatchWriteItemResult]
    with Marshallable[BatchWriteItemRequest, BatchWriteItemResult] {
      def responseHandler = new JsonResponseHandler(BatchWriteItemResultJsonUnmarshaller.getInstance())
    }
  case class BatchGetItem(req: BatchGetItemRequest)
    extends DynamoDBOp[BatchGetItemResult]
    with Marshallable[BatchGetItemRequest, BatchGetItemResult] {
      def responseHandler = new JsonResponseHandler(BatchGetItemResultJsonUnmarshaller.getInstance())
    }
  case class DeleteItem(req: DeleteItemRequest)
    extends DynamoDBOp[DeleteItemResult]
    with Marshallable[DeleteItemRequest, DeleteItemResult] {
      def responseHandler = new JsonResponseHandler(DeleteItemResultJsonUnmarshaller.getInstance())
    }

  type DynamoDBMonad[A] = Free[DynamoDBOp, A]
}
