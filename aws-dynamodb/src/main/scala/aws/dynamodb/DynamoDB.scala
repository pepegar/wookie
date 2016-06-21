package aws
package dynamodb

import com.amazonaws.services.dynamodbv2.model._
import com.amazonaws.transform.Marshaller
import com.amazonaws.Request
import scala.concurrent.Future
import cats.free.Free
import ast._
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import cats.data.Kleisli
import cats.Monad
import com.amazonaws.transform.Marshaller
import com.amazonaws.Request

object DynamoDB {
  import interpreter._
  import implicits._
  import Kleisli._

  type Result[A] = Kleisli[Future, Marshaller[Request[A], A], A]

  implicit val _: Monad[Result] = implicitly

  def endpoint = "https://dynamodb.us-east-1.amazonaws.com"

  def run[A](op: DynamoDBMonad[A])(
    implicit system: ActorSystem,
             mat: ActorMaterializer,
             m: Marshaller[Request[A], A]
  ): Future[A] = {
    val result = op foldMap futureInterpreter(endpoint)

    result.run(m)
  }
}

trait Instructions[P[_]] {
  def listTables(req: ListTablesRequest): P[ListTablesResult]
  def query(req: QueryRequest): P[QueryResult]
  def scan(req: ScanRequest): P[ScanResult]
  def updateItem(req: UpdateItemRequest): P[UpdateItemResult]
  def putItem(req: PutItemRequest): P[PutItemResult]
  def describeTable(req: DescribeTableRequest): P[DescribeTableResult]
  def createTable(req: CreateTableRequest): P[CreateTableResult]
  def updateTable(req: UpdateTableRequest): P[UpdateTableResult]
  def deleteTable(req: DeleteTableRequest): P[DeleteTableResult]
  def getItem(req: GetItemRequest): P[GetItemResult]
  def batchWriteItem(req: BatchWriteItemRequest): P[BatchWriteItemResult]
  def batchGetItem(req: BatchGetItemRequest): P[BatchGetItemResult]
  def deleteItem(req: DeleteItemRequest): P[DeleteItemResult]
}

object DynamoDBImpl extends Instructions[DynamoDBMonad] {
  def listTables(req: ListTablesRequest) = Free.liftF(ListTables(req))
  def query(req: QueryRequest) = Free.liftF(Query(req))
  def scan(req: ScanRequest) = Free.liftF(Scan(req))
  def updateItem(req: UpdateItemRequest) = Free.liftF(UpdateItem(req))
  def putItem(req: PutItemRequest) = Free.liftF(PutItem(req))
  def describeTable(req: DescribeTableRequest) = Free.liftF(DescribeTable(req))
  def createTable(req: CreateTableRequest) = Free.liftF(CreateTable(req))
  def updateTable(req: UpdateTableRequest) = Free.liftF(UpdateTable(req))
  def deleteTable(req: DeleteTableRequest) = Free.liftF(DeleteTable(req))
  def getItem(req: GetItemRequest) = Free.liftF(GetItem(req))
  def batchWriteItem(req: BatchWriteItemRequest) = Free.liftF(BatchWriteItem(req))
  def batchGetItem(req: BatchGetItemRequest) = Free.liftF(BatchGetItem(req))
  def deleteItem(req: DeleteItemRequest) = Free.liftF(DeleteItem(req))
}
