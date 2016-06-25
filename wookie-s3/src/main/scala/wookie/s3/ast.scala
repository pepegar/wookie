package wookie
package s3

import cats.free.Free
import com.amazonaws.services.s3.model._
import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.s3.model.transform.Unmarshallers
import com.amazonaws.services.s3.internal.{ Constants, S3XmlResponseHandler }
import com.amazonaws.services.s3.internal.{ S3ErrorResponseHandler, S3MetadataResponseHandler, S3ObjectResponseHandler }

object ast {
  import marshaller.Marshallable

  def defaultHandler = new S3XmlResponseHandler[Unit](null)

  sealed abstract class S3Op[A]
      extends Marshallable[A] with Product with Serializable {
    def req: AmazonWebServiceRequest
  }

  case class ListBuckets(req: ListBucketsRequest)
      extends S3Op[java.util.List[Bucket]]
      with Marshallable[java.util.List[Bucket]] {
    def responseHandler = new S3XmlResponseHandler[java.util.List[Bucket]](new Unmarshallers.ListBucketsUnmarshaller)
  }
  case class CreateBucket(req: CreateBucketRequest)
      extends S3Op[Unit]
      with Marshallable[Unit] {
    def responseHandler = defaultHandler
  }
  case class DeleteBucket(req: DeleteBucketRequest)
      extends S3Op[Unit]
      with Marshallable[Unit] {
    def responseHandler = defaultHandler
  }
  case class PutObject(req: PutObjectRequest)
      extends S3Op[ObjectMetadata]
      with Marshallable[ObjectMetadata] {
    def responseHandler = new S3MetadataResponseHandler()
  }
  case class GetObject(req: GetObjectRequest)
      extends S3Op[S3Object]
      with Marshallable[S3Object] {
    def responseHandler = new S3ObjectResponseHandler()
  }
  case class DeleteObject(req: DeleteObjectRequest)
      extends S3Op[Unit]
      with Marshallable[Unit] {
    def responseHandler = defaultHandler
  }
  case class ListObjects(req: ListObjectsRequest)
      extends S3Op[ObjectListing]
      with Marshallable[ObjectListing] {
    def responseHandler = new S3XmlResponseHandler[ObjectListing](new Unmarshallers.ListObjectsUnmarshaller)
  }

  type S3Monad[A] = Free[S3Op, A]
}
