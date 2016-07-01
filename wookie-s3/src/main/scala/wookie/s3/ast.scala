package wookie
package s3

import cats.free.Free
import com.amazonaws.services.s3.model._
import com.amazonaws.{ AmazonWebServiceRequest, Request }
import com.amazonaws.services.s3.model.transform.Unmarshallers
import com.amazonaws.transform.Marshaller
import com.amazonaws.services.s3.internal.{ Constants, S3XmlResponseHandler }
import com.amazonaws.services.s3.internal.{ S3ErrorResponseHandler, S3MetadataResponseHandler, S3ObjectResponseHandler }

object ast {
  import handler._

  type MM[A] = Marshaller[Request[A], A]

  def defaultHandler = new S3XmlResponseHandler[Unit](null)

  sealed abstract class S3Op[A]
      extends Handler[A] with Product with Serializable {
    def req: Request[_]
  }

  case class ListBuckets(value: ListBucketsRequest)(implicit M: MM[ListBucketsRequest])
      extends S3Op[java.util.List[Bucket]] {
    def responseHandler = new S3XmlResponseHandler[java.util.List[Bucket]](new Unmarshallers.ListBucketsUnmarshaller)
    def req = M.marshall(value)
  }
  case class CreateBucket(value: CreateBucketRequest)(implicit M: MM[CreateBucketRequest])
      extends S3Op[Unit] {
    def responseHandler = defaultHandler
    def req = M.marshall(value)
  }
  case class DeleteBucket(value: DeleteBucketRequest)(implicit M: MM[DeleteBucketRequest])
      extends S3Op[Unit] {
    def responseHandler = defaultHandler
    def req = M.marshall(value)
  }
  case class PutObject(value: PutObjectRequest)(implicit M: MM[PutObjectRequest])
      extends S3Op[ObjectMetadata] {
    def responseHandler = new S3MetadataResponseHandler()
    def req = M.marshall(value)
  }
  case class GetObject(value: GetObjectRequest)(implicit M: MM[GetObjectRequest])
      extends S3Op[S3Object] {
    def responseHandler = new S3ObjectResponseHandler()
    def req = M.marshall(value)
  }
  case class DeleteObject(value: DeleteObjectRequest)(implicit M: MM[DeleteObjectRequest])
      extends S3Op[Unit] {
    def responseHandler = defaultHandler
    def req = M.marshall(value)
  }
  case class ListObjects(value: ListObjectsRequest)(implicit M: MM[ListObjectsRequest])
      extends S3Op[ObjectListing] {
    def responseHandler = new S3XmlResponseHandler[ObjectListing](new Unmarshallers.ListObjectsUnmarshaller)
    def req = M.marshall(value)
  }

  type S3Monad[A] = Free[S3Op, A]
}
