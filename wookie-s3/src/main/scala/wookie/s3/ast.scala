package wookie
package s3

import com.amazonaws.services.s3.model._

object ast {

  sealed trait S3Op[T]
  case class ListBuckets(req: ListBucketsRequest) extends S3Op[java.util.List[Bucket]]
  case class CreateBucket(req: CreateBucketRequest) extends S3Op[Unit]
  case class DeleteBucket(req: DeleteBucketRequest) extends S3Op[Unit]
  case class PutObject(req: PutObjectRequest) extends S3Op[ObjectMetadata]
  case class GetObject(req: GetObjectRequest) extends S3Op[S3Object]
  case class DeleteObject(req: DeleteObjectRequest) extends S3Op[Unit]
  case class ListObjects(req: ListObjectsRequest) extends S3Op[ObjectListing]

}
