package wookie
package s3

import com.amazonaws.services.s3.model._
import com.amazonaws.http.HttpMethodName
import com.amazonaws.services.s3.model.transform.Unmarshallers
import com.amazonaws.services.s3.internal.{ Constants, S3XmlResponseHandler }
import com.amazonaws.services.s3.Headers
import com.amazonaws.transform.Marshaller
import com.amazonaws.{ AmazonWebServiceRequest, DefaultRequest, Request }
import com.amazonaws.services.s3.internal.{ S3ErrorResponseHandler, S3MetadataResponseHandler, S3ObjectResponseHandler }

object implicits {
  // As well here, this is gently stolen from sclasen/akka-aws
  class S3Marshaller[X <: AmazonWebServiceRequest](httpMethod: HttpMethodName)
      extends Marshaller[Request[X], X] {
    def marshall(originalRequest: X): Request[X] = {
      val request = new DefaultRequest[X](originalRequest, Constants.S3_SERVICE_NAME)
      request.setHttpMethod(httpMethod)
      request.addHeader("x-amz-content-sha256", "required")

      originalRequest.getClass.getMethods.find(_.getName == "getBucketName").map { method ⇒
        val bucketName: String = method.invoke(originalRequest).asInstanceOf[String]
        request.setResourcePath(s"/${bucketName}")

        originalRequest.getClass.getMethods.find(_.getName == "getKey").map { method ⇒
          val key: String = method.invoke(originalRequest).asInstanceOf[String]
          request.setResourcePath(s"/${bucketName}/${key}")
        }
      }

      request
    }
  }

  implicit val listBucketsM = new S3Marshaller[ListBucketsRequest](HttpMethodName.GET)
  implicit val listBucketsU = new S3XmlResponseHandler[java.util.List[Bucket]](new Unmarshallers.ListBucketsUnmarshaller)

  implicit val createBucketM = new S3Marshaller[CreateBucketRequest](HttpMethodName.PUT)
  implicit val deleteBucketM = new S3Marshaller[DeleteBucketRequest](HttpMethodName.DELETE)

  implicit val putObjectM = new S3Marshaller[PutObjectRequest](HttpMethodName.PUT) {
    override def marshall(putObject: PutObjectRequest): Request[PutObjectRequest] = {
      val request = super.marshall(putObject)

      if (Option(putObject.getFile).isDefined) {
        throw new Exception("File upload not supported")
      } else {
        import scala.collection.JavaConversions._
        request.getHeaders.putAll(putObject.getMetadata.getRawMetadata.map { case (k, v) ⇒ k → v.toString })
        request.getHeaders.putAll(putObject.getMetadata.getUserMetadata.map { case (k, v) ⇒ s"${Headers.S3_USER_METADATA_PREFIX}-$k" → v })
        Option(putObject.getRedirectLocation).foreach(request.getHeaders.put(Headers.REDIRECT_LOCATION, _))
        Option(putObject.getStorageClass).foreach(request.getHeaders.put(Headers.STORAGE_CLASS, _))
        Option(putObject.getCannedAcl).foreach(acl ⇒ request.getHeaders.put(Headers.S3_CANNED_ACL, acl.toString))
        // TODO, handle putObject.getAccessControlList, cf: http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html
        request.setContent(putObject.getInputStream)
        request
      }
    }
  }
  implicit val putObjectU = new S3MetadataResponseHandler()
  implicit val getObjectM = new S3Marshaller[GetObjectRequest](HttpMethodName.GET)
  implicit val deleteObjectM = new S3Marshaller[DeleteObjectRequest](HttpMethodName.DELETE)

  implicit val listObjectsM = new S3Marshaller[ListObjectsRequest](HttpMethodName.GET) {
    override def marshall(listObjects: ListObjectsRequest): Request[ListObjectsRequest] = {
      val request = super.marshall(listObjects)
      Option(listObjects.getPrefix).foreach(request.addParameter("prefix", _))
      Option(listObjects.getMarker).foreach(request.addParameter("marker", _))
      Option(listObjects.getDelimiter).foreach(request.addParameter("delimiter", _))
      Option(listObjects.getMaxKeys).foreach(maxKeys ⇒ request.addParameter("max-keys", maxKeys.toString))
      Option(listObjects.getEncodingType).foreach(request.addParameter("encoding-type", _))
      request
    }
  }

  implicit val listObjectsU = new S3XmlResponseHandler[ObjectListing](new Unmarshallers.ListObjectsUnmarshaller)

  implicit val objectU = new S3ObjectResponseHandler()
  implicit val voidU = new S3XmlResponseHandler[Unit](null)
}
