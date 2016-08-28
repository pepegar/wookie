package wookie.s3

import java.io.{ File, InputStream }
import java.util.List

import cats.free.Free
import com.amazonaws.{ AmazonWebServiceResponse, Request }
import com.amazonaws.http.HttpResponseHandler
import com.amazonaws.services.s3.model._
import com.amazonaws.transform.Marshaller
import wookie.handler
import wookie.s3.implicits._

object algebra {

  import handler._

  type S3IO[A] = Free[S3Op, A]

  sealed trait S3Op[A]
      extends Handler[A]
      with Product with Serializable {
    def marshalledReq: Request[_]
  }

  object S3Op {

    case class ListBuckets()(implicit M: Marshaller[Request[ListBucketsRequest], ListBucketsRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[List[Bucket]]])
        extends S3Op[List[Bucket]] {
      def amzReq = new ListBucketsRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class CreateBucket(a: String, b: String)(implicit M: Marshaller[Request[CreateBucketRequest], CreateBucketRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[Unit]])
        extends S3Op[Unit] {
      def amzReq = new CreateBucketRequest(a, b)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class CreateBucket1(a: String, b: Region)(implicit M: Marshaller[Request[CreateBucketRequest], CreateBucketRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[Unit]])
        extends S3Op[Unit] {
      def amzReq = new CreateBucketRequest(a, b)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class CreateBucket2(a: String)(implicit M: Marshaller[Request[CreateBucketRequest], CreateBucketRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[Unit]])
        extends S3Op[Unit] {
      def amzReq = new CreateBucketRequest(a)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class DeleteBucket(a: String)(implicit M: Marshaller[Request[DeleteBucketRequest], DeleteBucketRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[Unit]])
        extends S3Op[Unit] {
      def amzReq = new DeleteBucketRequest(a)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class PutObject(a: String, b: String, c: InputStream, d: ObjectMetadata)(implicit M: Marshaller[Request[PutObjectRequest], PutObjectRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[ObjectMetadata]])
        extends S3Op[ObjectMetadata] {
      def amzReq = new PutObjectRequest(a, b, c, d)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class PutObject1(a: String, b: String, c: String)(implicit M: Marshaller[Request[PutObjectRequest], PutObjectRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[ObjectMetadata]])
        extends S3Op[ObjectMetadata] {
      def amzReq = new PutObjectRequest(a, b, c)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class PutObject2(a: String, b: String, c: File)(implicit M: Marshaller[Request[PutObjectRequest], PutObjectRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[ObjectMetadata]])
        extends S3Op[ObjectMetadata] {
      def amzReq = new PutObjectRequest(a, b, c)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class GetObject(a: String, b: String, c: Boolean)(implicit M: Marshaller[Request[GetObjectRequest], GetObjectRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[S3Object]])
        extends S3Op[S3Object] {
      def amzReq = new GetObjectRequest(a, b, c)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class GetObject1(a: S3ObjectId)(implicit M: Marshaller[Request[GetObjectRequest], GetObjectRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[S3Object]])
        extends S3Op[S3Object] {
      def amzReq = new GetObjectRequest(a)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class GetObject2(a: String, b: String, c: String)(implicit M: Marshaller[Request[GetObjectRequest], GetObjectRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[S3Object]])
        extends S3Op[S3Object] {
      def amzReq = new GetObjectRequest(a, b, c)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class GetObject3(a: String, b: String)(implicit M: Marshaller[Request[GetObjectRequest], GetObjectRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[S3Object]])
        extends S3Op[S3Object] {
      def amzReq = new GetObjectRequest(a, b)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class DeleteObject(a: String, b: String)(implicit M: Marshaller[Request[DeleteObjectRequest], DeleteObjectRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[Unit]])
        extends S3Op[Unit] {
      def amzReq = new DeleteObjectRequest(a, b)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class ListObjects()(implicit M: Marshaller[Request[ListObjectsRequest], ListObjectsRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[ObjectListing]])
        extends S3Op[ObjectListing] {
      def amzReq = new ListObjectsRequest()
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }
    case class ListObjects1(a: String, b: String, c: String, d: String, e: Integer)(implicit M: Marshaller[Request[ListObjectsRequest], ListObjectsRequest], RH: HttpResponseHandler[AmazonWebServiceResponse[ObjectListing]])
        extends S3Op[ObjectListing] {
      def amzReq = new ListObjectsRequest(a, b, c, d, e)
      def marshalledReq = M.marshall(amzReq)
      def responseHandler = RH
    }

  }

  object ops {

    import S3Op._

    trait S3Ops[P[_]] {
      def listBucketsRequest: P[List[Bucket]]
      def createBucketRequest(a: String, b: String): P[Unit]
      def createBucketRequest(a: String, b: Region): P[Unit]
      def createBucketRequest(a: String): P[Unit]
      def deleteBucketRequest(a: String): P[Unit]
      def putObjectRequest(a: String, b: String, c: InputStream, d: ObjectMetadata): P[ObjectMetadata]
      def putObjectRequest(a: String, b: String, c: String): P[ObjectMetadata]
      def putObjectRequest(a: String, b: String, c: File): P[ObjectMetadata]
      def getObjectRequest(a: String, b: String, c: Boolean): P[S3Object]
      def getObjectRequest(a: S3ObjectId): P[S3Object]
      def getObjectRequest(a: String, b: String, c: String): P[S3Object]
      def getObjectRequest(a: String, b: String): P[S3Object]
      def deleteObjectRequest(a: String, b: String): P[Unit]
      def listObjectsRequest: P[ObjectListing]
      def listObjectsRequest(a: String, b: String, c: String, d: String, e: Integer): P[ObjectListing]

    }

    object S3Ops extends S3Ops[S3IO] {
      def listBucketsRequest: S3IO[List[Bucket]] =
        Free.liftF(ListBuckets())
      def createBucketRequest(a: String, b: String): S3IO[Unit] =
        Free.liftF(CreateBucket(a, b))
      def createBucketRequest(a: String, b: Region): S3IO[Unit] =
        Free.liftF(CreateBucket1(a, b))
      def createBucketRequest(a: String): S3IO[Unit] =
        Free.liftF(CreateBucket2(a))
      def deleteBucketRequest(a: String): S3IO[Unit] =
        Free.liftF(DeleteBucket(a))
      def putObjectRequest(a: String, b: String, c: InputStream, d: ObjectMetadata): S3IO[ObjectMetadata] =
        Free.liftF(PutObject(a, b, c, d))
      def putObjectRequest(a: String, b: String, c: String): S3IO[ObjectMetadata] =
        Free.liftF(PutObject1(a, b, c))
      def putObjectRequest(a: String, b: String, c: File): S3IO[ObjectMetadata] =
        Free.liftF(PutObject2(a, b, c))
      def getObjectRequest(a: String, b: String, c: Boolean): S3IO[S3Object] =
        Free.liftF(GetObject(a, b, c))
      def getObjectRequest(a: S3ObjectId): S3IO[S3Object] =
        Free.liftF(GetObject1(a))
      def getObjectRequest(a: String, b: String, c: String): S3IO[S3Object] =
        Free.liftF(GetObject2(a, b, c))
      def getObjectRequest(a: String, b: String): S3IO[S3Object] =
        Free.liftF(GetObject3(a, b))
      def deleteObjectRequest(a: String, b: String): S3IO[Unit] =
        Free.liftF(DeleteObject(a, b))
      def listObjectsRequest: S3IO[ObjectListing] =
        Free.liftF(ListObjects())
      def listObjectsRequest(a: String, b: String, c: String, d: String, e: Integer): S3IO[ObjectListing] =
        Free.liftF(ListObjects1(a, b, c, d, e))

    }
  }

}
