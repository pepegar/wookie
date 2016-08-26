package wookie
package dynamodb

import java.lang.Boolean
import java.util.{ List, Map }

import cats.free.Free
import com.amazonaws.{ AmazonWebServiceResponse, Request }
import com.amazonaws.http.HttpResponseHandler
import com.amazonaws.services.dynamodbv2.model._
import com.amazonaws.transform.Marshaller
import wookie.dynamodb.implicits._

object algebra {

  import handler._

  type DynamoDBIO[A] = Free[DynamoDBOp, A]

  sealed trait DynamoDBOp[A]
      extends Handler[A]
      with Product with Serializable {
    def marshalledReq: Request[_]
  }

  object DynamoDBOp {

    case class ListTables(a: String, b: Integer)(implicit M: Marshaller[Request[ListTablesRequest], ListTablesRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[ListTablesResult]])
        extends DynamoDBOp[ListTablesResult] {
      def amzReq = new ListTablesRequest(a, b)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class ListTables1(a: String)(implicit M: Marshaller[Request[ListTablesRequest], ListTablesRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[ListTablesResult]])
        extends DynamoDBOp[ListTablesResult] {
      def amzReq = new ListTablesRequest(a)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class ListTables2()(implicit M: Marshaller[Request[ListTablesRequest], ListTablesRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[ListTablesResult]])
        extends DynamoDBOp[ListTablesResult] {
      def amzReq = new ListTablesRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class Query()(implicit M: Marshaller[Request[QueryRequest], QueryRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[QueryResult]])
        extends DynamoDBOp[QueryResult] {
      def amzReq = new QueryRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class Query1(a: String)(implicit M: Marshaller[Request[QueryRequest], QueryRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[QueryResult]])
        extends DynamoDBOp[QueryResult] {
      def amzReq = new QueryRequest(a)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class Scan()(implicit M: Marshaller[Request[ScanRequest], ScanRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[ScanResult]])
        extends DynamoDBOp[ScanResult] {
      def amzReq = new ScanRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class Scan1(a: String)(implicit M: Marshaller[Request[ScanRequest], ScanRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[ScanResult]])
        extends DynamoDBOp[ScanResult] {
      def amzReq = new ScanRequest(a)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class UpdateItem(a: String, b: Map[String, AttributeValue], c: Map[String, AttributeValueUpdate], d: ReturnValue)(implicit M: Marshaller[Request[UpdateItemRequest], UpdateItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[UpdateItemResult]])
        extends DynamoDBOp[UpdateItemResult] {
      def amzReq = new UpdateItemRequest(a, b, c, d)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class UpdateItem1(a: String, b: Map[String, AttributeValue], c: Map[String, AttributeValueUpdate], d: String)(implicit M: Marshaller[Request[UpdateItemRequest], UpdateItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[UpdateItemResult]])
        extends DynamoDBOp[UpdateItemResult] {
      def amzReq = new UpdateItemRequest(a, b, c, d)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class UpdateItem2(a: String, b: Map[String, AttributeValue], c: Map[String, AttributeValueUpdate])(implicit M: Marshaller[Request[UpdateItemRequest], UpdateItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[UpdateItemResult]])
        extends DynamoDBOp[UpdateItemResult] {
      def amzReq = new UpdateItemRequest(a, b, c)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class UpdateItem3()(implicit M: Marshaller[Request[UpdateItemRequest], UpdateItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[UpdateItemResult]])
        extends DynamoDBOp[UpdateItemResult] {
      def amzReq = new UpdateItemRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class PutItem(a: String, b: Map[String, AttributeValue], c: ReturnValue)(implicit M: Marshaller[Request[PutItemRequest], PutItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[PutItemResult]])
        extends DynamoDBOp[PutItemResult] {
      def amzReq = new PutItemRequest(a, b, c)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class PutItem1(a: String, b: Map[String, AttributeValue], c: String)(implicit M: Marshaller[Request[PutItemRequest], PutItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[PutItemResult]])
        extends DynamoDBOp[PutItemResult] {
      def amzReq = new PutItemRequest(a, b, c)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class PutItem2(a: String, b: Map[String, AttributeValue])(implicit M: Marshaller[Request[PutItemRequest], PutItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[PutItemResult]])
        extends DynamoDBOp[PutItemResult] {
      def amzReq = new PutItemRequest(a, b)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class PutItem3()(implicit M: Marshaller[Request[PutItemRequest], PutItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[PutItemResult]])
        extends DynamoDBOp[PutItemResult] {
      def amzReq = new PutItemRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class DescribeTable()(implicit M: Marshaller[Request[DescribeTableRequest], DescribeTableRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[DescribeTableResult]])
        extends DynamoDBOp[DescribeTableResult] {
      def amzReq = new DescribeTableRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class DescribeTable1(a: String)(implicit M: Marshaller[Request[DescribeTableRequest], DescribeTableRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[DescribeTableResult]])
        extends DynamoDBOp[DescribeTableResult] {
      def amzReq = new DescribeTableRequest(a)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class CreateTable(a: List[AttributeDefinition], b: String, c: List[KeySchemaElement], d: ProvisionedThroughput)(implicit M: Marshaller[Request[CreateTableRequest], CreateTableRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[CreateTableResult]])
        extends DynamoDBOp[CreateTableResult] {
      def amzReq = new CreateTableRequest(a, b, c, d)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class CreateTable1(a: String, b: List[KeySchemaElement])(implicit M: Marshaller[Request[CreateTableRequest], CreateTableRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[CreateTableResult]])
        extends DynamoDBOp[CreateTableResult] {
      def amzReq = new CreateTableRequest(a, b)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class CreateTable2()(implicit M: Marshaller[Request[CreateTableRequest], CreateTableRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[CreateTableResult]])
        extends DynamoDBOp[CreateTableResult] {
      def amzReq = new CreateTableRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class UpdateTable()(implicit M: Marshaller[Request[UpdateTableRequest], UpdateTableRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[UpdateTableResult]])
        extends DynamoDBOp[UpdateTableResult] {
      def amzReq = new UpdateTableRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class UpdateTable1(a: String, b: ProvisionedThroughput)(implicit M: Marshaller[Request[UpdateTableRequest], UpdateTableRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[UpdateTableResult]])
        extends DynamoDBOp[UpdateTableResult] {
      def amzReq = new UpdateTableRequest(a, b)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class DeleteTable()(implicit M: Marshaller[Request[DeleteTableRequest], DeleteTableRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[DeleteTableResult]])
        extends DynamoDBOp[DeleteTableResult] {
      def amzReq = new DeleteTableRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class DeleteTable1(a: String)(implicit M: Marshaller[Request[DeleteTableRequest], DeleteTableRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[DeleteTableResult]])
        extends DynamoDBOp[DeleteTableResult] {
      def amzReq = new DeleteTableRequest(a)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class GetItem(a: String, b: Map[String, AttributeValue], c: Boolean)(implicit M: Marshaller[Request[GetItemRequest], GetItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[GetItemResult]])
        extends DynamoDBOp[GetItemResult] {
      def amzReq = new GetItemRequest(a, b, c)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class GetItem1(a: String, b: Map[String, AttributeValue])(implicit M: Marshaller[Request[GetItemRequest], GetItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[GetItemResult]])
        extends DynamoDBOp[GetItemResult] {
      def amzReq = new GetItemRequest(a, b)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class GetItem2()(implicit M: Marshaller[Request[GetItemRequest], GetItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[GetItemResult]])
        extends DynamoDBOp[GetItemResult] {
      def amzReq = new GetItemRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class BatchWriteItem()(implicit M: Marshaller[Request[BatchWriteItemRequest], BatchWriteItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[BatchWriteItemResult]])
        extends DynamoDBOp[BatchWriteItemResult] {
      def amzReq = new BatchWriteItemRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class BatchWriteItem1(a: Map[String, List[WriteRequest]])(implicit M: Marshaller[Request[BatchWriteItemRequest], BatchWriteItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[BatchWriteItemResult]])
        extends DynamoDBOp[BatchWriteItemResult] {
      def amzReq = new BatchWriteItemRequest(a)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class BatchGetItem(a: Map[String, KeysAndAttributes], b: ReturnConsumedCapacity)(implicit M: Marshaller[Request[BatchGetItemRequest], BatchGetItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[BatchGetItemResult]])
        extends DynamoDBOp[BatchGetItemResult] {
      def amzReq = new BatchGetItemRequest(a, b)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class BatchGetItem1(a: Map[String, KeysAndAttributes], b: String)(implicit M: Marshaller[Request[BatchGetItemRequest], BatchGetItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[BatchGetItemResult]])
        extends DynamoDBOp[BatchGetItemResult] {
      def amzReq = new BatchGetItemRequest(a, b)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class BatchGetItem2(a: Map[String, KeysAndAttributes])(implicit M: Marshaller[Request[BatchGetItemRequest], BatchGetItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[BatchGetItemResult]])
        extends DynamoDBOp[BatchGetItemResult] {
      def amzReq = new BatchGetItemRequest(a)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class BatchGetItem3()(implicit M: Marshaller[Request[BatchGetItemRequest], BatchGetItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[BatchGetItemResult]])
        extends DynamoDBOp[BatchGetItemResult] {
      def amzReq = new BatchGetItemRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class DeleteItem(a: String, b: Map[String, AttributeValue], c: ReturnValue)(implicit M: Marshaller[Request[DeleteItemRequest], DeleteItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[DeleteItemResult]])
        extends DynamoDBOp[DeleteItemResult] {
      def amzReq = new DeleteItemRequest(a, b, c)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class DeleteItem1(a: String, b: Map[String, AttributeValue], c: String)(implicit M: Marshaller[Request[DeleteItemRequest], DeleteItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[DeleteItemResult]])
        extends DynamoDBOp[DeleteItemResult] {
      def amzReq = new DeleteItemRequest(a, b, c)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class DeleteItem2(a: String, b: Map[String, AttributeValue])(implicit M: Marshaller[Request[DeleteItemRequest], DeleteItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[DeleteItemResult]])
        extends DynamoDBOp[DeleteItemResult] {
      def amzReq = new DeleteItemRequest(a, b)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class DeleteItem3()(implicit M: Marshaller[Request[DeleteItemRequest], DeleteItemRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[DeleteItemResult]])
        extends DynamoDBOp[DeleteItemResult] {
      def amzReq = new DeleteItemRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }

  }

  object ops {

    import DynamoDBOp._

    trait DynamoDBOps[P[_]] {
      def listTablesRequest(a: String, b: Integer): P[ListTablesResult]
      def listTablesRequest(a: String): P[ListTablesResult]
      def listTablesRequest: P[ListTablesResult]
      def queryRequest: P[QueryResult]
      def queryRequest(a: String): P[QueryResult]
      def scanRequest: P[ScanResult]
      def scanRequest(a: String): P[ScanResult]
      def updateItemRequest(a: String, b: Map[String, AttributeValue], c: Map[String, AttributeValueUpdate], d: ReturnValue): P[UpdateItemResult]
      def updateItemRequest(a: String, b: Map[String, AttributeValue], c: Map[String, AttributeValueUpdate], d: String): P[UpdateItemResult]
      def updateItemRequest(a: String, b: Map[String, AttributeValue], c: Map[String, AttributeValueUpdate]): P[UpdateItemResult]
      def updateItemRequest: P[UpdateItemResult]
      def putItemRequest(a: String, b: Map[String, AttributeValue], c: ReturnValue): P[PutItemResult]
      def putItemRequest(a: String, b: Map[String, AttributeValue], c: String): P[PutItemResult]
      def putItemRequest(a: String, b: Map[String, AttributeValue]): P[PutItemResult]
      def putItemRequest: P[PutItemResult]
      def describeTableRequest: P[DescribeTableResult]
      def describeTableRequest(a: String): P[DescribeTableResult]
      def createTableRequest(a: List[AttributeDefinition], b: String, c: List[KeySchemaElement], d: ProvisionedThroughput): P[CreateTableResult]
      def createTableRequest(a: String, b: List[KeySchemaElement]): P[CreateTableResult]
      def createTableRequest: P[CreateTableResult]
      def updateTableRequest: P[UpdateTableResult]
      def updateTableRequest(a: String, b: ProvisionedThroughput): P[UpdateTableResult]
      def deleteTableRequest: P[DeleteTableResult]
      def deleteTableRequest(a: String): P[DeleteTableResult]
      def getItemRequest(a: String, b: Map[String, AttributeValue], c: Boolean): P[GetItemResult]
      def getItemRequest(a: String, b: Map[String, AttributeValue]): P[GetItemResult]
      def getItemRequest: P[GetItemResult]
      def batchWriteItemRequest: P[BatchWriteItemResult]
      def batchWriteItemRequest(a: Map[String, List[WriteRequest]]): P[BatchWriteItemResult]
      def batchGetItemRequest(a: Map[String, KeysAndAttributes], b: ReturnConsumedCapacity): P[BatchGetItemResult]
      def batchGetItemRequest(a: Map[String, KeysAndAttributes], b: String): P[BatchGetItemResult]
      def batchGetItemRequest(a: Map[String, KeysAndAttributes]): P[BatchGetItemResult]
      def batchGetItemRequest: P[BatchGetItemResult]
      def deleteItemRequest(a: String, b: Map[String, AttributeValue], c: ReturnValue): P[DeleteItemResult]
      def deleteItemRequest(a: String, b: Map[String, AttributeValue], c: String): P[DeleteItemResult]
      def deleteItemRequest(a: String, b: Map[String, AttributeValue]): P[DeleteItemResult]
      def deleteItemRequest: P[DeleteItemResult]

    }

    object DynamoDBOps extends DynamoDBOps[DynamoDBIO] {
      def listTablesRequest(a: String, b: Integer): DynamoDBIO[ListTablesResult] =
        Free.liftF(ListTables(a, b))
      def listTablesRequest(a: String): DynamoDBIO[ListTablesResult] =
        Free.liftF(ListTables1(a))
      def listTablesRequest: DynamoDBIO[ListTablesResult] =
        Free.liftF(ListTables2())
      def queryRequest: DynamoDBIO[QueryResult] =
        Free.liftF(Query())
      def queryRequest(a: String): DynamoDBIO[QueryResult] =
        Free.liftF(Query1(a))
      def scanRequest: DynamoDBIO[ScanResult] =
        Free.liftF(Scan())
      def scanRequest(a: String): DynamoDBIO[ScanResult] =
        Free.liftF(Scan1(a))
      def updateItemRequest(a: String, b: Map[String, AttributeValue], c: Map[String, AttributeValueUpdate], d: ReturnValue): DynamoDBIO[UpdateItemResult] =
        Free.liftF(UpdateItem(a, b, c, d))
      def updateItemRequest(a: String, b: Map[String, AttributeValue], c: Map[String, AttributeValueUpdate], d: String): DynamoDBIO[UpdateItemResult] =
        Free.liftF(UpdateItem1(a, b, c, d))
      def updateItemRequest(a: String, b: Map[String, AttributeValue], c: Map[String, AttributeValueUpdate]): DynamoDBIO[UpdateItemResult] =
        Free.liftF(UpdateItem2(a, b, c))
      def updateItemRequest: DynamoDBIO[UpdateItemResult] =
        Free.liftF(UpdateItem3())
      def putItemRequest(a: String, b: Map[String, AttributeValue], c: ReturnValue): DynamoDBIO[PutItemResult] =
        Free.liftF(PutItem(a, b, c))
      def putItemRequest(a: String, b: Map[String, AttributeValue], c: String): DynamoDBIO[PutItemResult] =
        Free.liftF(PutItem1(a, b, c))
      def putItemRequest(a: String, b: Map[String, AttributeValue]): DynamoDBIO[PutItemResult] =
        Free.liftF(PutItem2(a, b))
      def putItemRequest: DynamoDBIO[PutItemResult] =
        Free.liftF(PutItem3())
      def describeTableRequest: DynamoDBIO[DescribeTableResult] =
        Free.liftF(DescribeTable())
      def describeTableRequest(a: String): DynamoDBIO[DescribeTableResult] =
        Free.liftF(DescribeTable1(a))
      def createTableRequest(a: List[AttributeDefinition], b: String, c: List[KeySchemaElement], d: ProvisionedThroughput): DynamoDBIO[CreateTableResult] =
        Free.liftF(CreateTable(a, b, c, d))
      def createTableRequest(a: String, b: List[KeySchemaElement]): DynamoDBIO[CreateTableResult] =
        Free.liftF(CreateTable1(a, b))
      def createTableRequest: DynamoDBIO[CreateTableResult] =
        Free.liftF(CreateTable2())
      def updateTableRequest: DynamoDBIO[UpdateTableResult] =
        Free.liftF(UpdateTable())
      def updateTableRequest(a: String, b: ProvisionedThroughput): DynamoDBIO[UpdateTableResult] =
        Free.liftF(UpdateTable1(a, b))
      def deleteTableRequest: DynamoDBIO[DeleteTableResult] =
        Free.liftF(DeleteTable())
      def deleteTableRequest(a: String): DynamoDBIO[DeleteTableResult] =
        Free.liftF(DeleteTable1(a))
      def getItemRequest(a: String, b: Map[String, AttributeValue], c: Boolean): DynamoDBIO[GetItemResult] =
        Free.liftF(GetItem(a, b, c))
      def getItemRequest(a: String, b: Map[String, AttributeValue]): DynamoDBIO[GetItemResult] =
        Free.liftF(GetItem1(a, b))
      def getItemRequest: DynamoDBIO[GetItemResult] =
        Free.liftF(GetItem2())
      def batchWriteItemRequest: DynamoDBIO[BatchWriteItemResult] =
        Free.liftF(BatchWriteItem())
      def batchWriteItemRequest(a: Map[String, List[WriteRequest]]): DynamoDBIO[BatchWriteItemResult] =
        Free.liftF(BatchWriteItem1(a))
      def batchGetItemRequest(a: Map[String, KeysAndAttributes], b: ReturnConsumedCapacity): DynamoDBIO[BatchGetItemResult] =
        Free.liftF(BatchGetItem(a, b))
      def batchGetItemRequest(a: Map[String, KeysAndAttributes], b: String): DynamoDBIO[BatchGetItemResult] =
        Free.liftF(BatchGetItem1(a, b))
      def batchGetItemRequest(a: Map[String, KeysAndAttributes]): DynamoDBIO[BatchGetItemResult] =
        Free.liftF(BatchGetItem2(a))
      def batchGetItemRequest: DynamoDBIO[BatchGetItemResult] =
        Free.liftF(BatchGetItem3())
      def deleteItemRequest(a: String, b: Map[String, AttributeValue], c: ReturnValue): DynamoDBIO[DeleteItemResult] =
        Free.liftF(DeleteItem(a, b, c))
      def deleteItemRequest(a: String, b: Map[String, AttributeValue], c: String): DynamoDBIO[DeleteItemResult] =
        Free.liftF(DeleteItem1(a, b, c))
      def deleteItemRequest(a: String, b: Map[String, AttributeValue]): DynamoDBIO[DeleteItemResult] =
        Free.liftF(DeleteItem2(a, b))
      def deleteItemRequest: DynamoDBIO[DeleteItemResult] =
        Free.liftF(DeleteItem3())

    }
  }

}
