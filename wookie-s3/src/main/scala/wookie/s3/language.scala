package wookie
package s3

import com.amazonaws.services.s3.model._

import cats.free._

object language {

  import ast._

  trait Ops[P[_]] {
    def listBuckets(req: ListBucketsRequest): P[java.util.List[Bucket]]
    def createBucket(req: CreateBucketRequest): P[Unit]
    def deleteBucket(req: DeleteBucketRequest): P[Unit]
    def putObject(req: PutObjectRequest): P[ObjectMetadata]
    def getObject(req: GetObjectRequest): P[S3Object]
    def deleteObject(req: DeleteObjectRequest): P[Unit]
    def listObjects(req: ListObjectsRequest): P[ObjectListing]
  }

  object Ops extends Ops[S3Monad] {
    def listBuckets(req: ListBucketsRequest): S3Monad[java.util.List[Bucket]] = Free.liftF(ListBuckets(req))
    def createBucket(req: CreateBucketRequest): S3Monad[Unit] = Free.liftF(CreateBucket(req))
    def deleteBucket(req: DeleteBucketRequest): S3Monad[Unit] = Free.liftF(DeleteBucket(req))
    def putObject(req: PutObjectRequest): S3Monad[ObjectMetadata] = Free.liftF(PutObject(req))
    def getObject(req: GetObjectRequest): S3Monad[S3Object] = Free.liftF(GetObject(req))
    def deleteObject(req: DeleteObjectRequest): S3Monad[Unit] = Free.liftF(DeleteObject(req))
    def listObjects(req: ListObjectsRequest): S3Monad[ObjectListing] = Free.liftF(ListObjects(req))
  }
}
