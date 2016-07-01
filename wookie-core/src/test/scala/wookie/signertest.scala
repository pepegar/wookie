package wookie

import org.specs2._
import com.amazonaws.auth.{ Signer ⇒ AWSSigner, BasicAWSCredentials }
import com.amazonaws.services.dynamodbv2.model._
import com.amazonaws.{ DefaultRequest, Request }

import signer._

class SignerSpec extends Specification {
  def is = s2"""
  This specifies the Signer trait

  The Signer trait should
    set resourcePath and endpoint $e1
    add Authorization header      $e2
  """

  val resourcePath = "http://endpointfordynamodb"
  val credentials = new BasicAWSCredentials("accessKey", "secretAccessKey")
  val request: Request[ListTablesRequest] = new DefaultRequest(new ListTablesRequest, "dynamodb")
  val signer = Signer(
    resourcePath,
    "dynamodb",
    credentials
  )

  def e1 = {
    val newReq = signer.sign(request)

    newReq.getResourcePath === resourcePath
    newReq.getEndpoint === new java.net.URI(resourcePath)
  }

  def e2 = {
    val newReq = signer.sign(request)

    newReq.getHeaders.get("Authorization") !== null
  }

}
