package wookie
package akkahttp

import java.io.ByteArrayInputStream
import java.net.{ URI, URLEncoder }

import cats.data.Xor

import akka.actor.{ ActorSystem, _ }
import akka.event.LoggingAdapter
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding._
import akka.http.scaladsl.model.MediaType.NotCompressible
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{ MediaType, _ }
import akka.http.scaladsl.settings.ClientConnectionSettings
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.util.{ ByteString, Timeout }

import com.amazonaws.auth._
import com.amazonaws.http.{ HttpMethodName, HttpResponseHandler, HttpResponse ⇒ AWSHttpResponse }
import com.amazonaws.{ AmazonServiceException, AmazonWebServiceResponse, DefaultRequest, Request }

import scala.collection.JavaConverters._
import scala.concurrent.{ Future, ExecutionContext }
import scala.concurrent.duration._

object http {

  val timeout: Timeout = Timeout(5 seconds)

  val defaultContentType = "application/x-amz-json-1.0"

  def errorResponseHandler: HttpResponseHandler[AmazonServiceException] = null

  def createHttpRequest[T](request: Request[T]): HttpRequest = {
    require(request.getEndpoint() != null, "Endpoint must be set")

    val contentType = Option(request.getHeaders.get("Content-Type"))
    request.getHeaders.remove("Host")
    request.getHeaders.remove("User-Agent")
    request.getHeaders.remove("Content-Length")
    request.getHeaders.remove("Content-Type")

    val path: String = if (request.getResourcePath == "" || request.getResourcePath == null) {
      "/"
    } else {
      request.getResourcePath
    }

    if (request.getContent != null) {
      val body: Array[Byte] = Stream.continually(request.getContent.read).takeWhile(-1 != _).map(_.toByte).toArray
      val Array(main, secondary) = contentType.getOrElse(defaultContentType).split("/")
      val mediaType = MediaType.customBinary(main, secondary, NotCompressible)
      HttpRequest(
        request.getHttpMethod,
        Uri(request.getEndpoint.toString),
        headers(request),
        HttpEntity(mediaType, body),
        HttpProtocols.`HTTP/1.1`
      )
    } else {
      val method: HttpMethod = request.getHttpMethod
      method match {
        case HttpMethods.POST ⇒ {
          headers(request) match {
            case Nil     ⇒ Post(path, formData(request))
            case x :: xs ⇒ Post(path, formData(request)) ~> addHeaders(x, xs: _*)
          }
        }
        case HttpMethods.PUT ⇒
          headers(request) match {
            case Nil     ⇒ Put(path, formData(request))
            case x :: xs ⇒ Put(path, formData(request)) ~> addHeaders(x, xs: _*)
          }
        case method ⇒
          val uri = Uri(path = Uri.Path(path)).withRawQueryString(encodeQuery(request))
          HttpRequest(method, uri, headers(request))
      }
    }
  }

  def sendRequest(req: HttpRequest)(
    implicit
    system: ActorSystem,
    mat:    ActorMaterializer
  ): Future[HttpResponse] = Http().singleRequest(req)

  def parseResponse[T](serviceName: String, response: HttpResponse)(
    implicit
    handler: HttpResponseHandler[AmazonWebServiceResponse[T]],
    system:  ActorSystem,
    mat:     ActorMaterializer
  ): Future[AmazonServiceException Xor T] = {
    implicit val ec = system.dispatcher
    val req = new DefaultRequest[T](serviceName)
    val awsResp = new AWSHttpResponse(req, null)
    val futureBs: Future[ByteString] = response.entity.toStrict(timeout.duration).map { _.data }

    futureBs map { bs ⇒
      awsResp.setContent(new ByteArrayInputStream(bs.toArray))
      awsResp.setStatusCode(response.status.intValue)
      awsResp.setStatusText(response.status.defaultMessage)
      if (200 <= awsResp.getStatusCode && awsResp.getStatusCode < 300) {
        val handle: AmazonWebServiceResponse[T] = handler.handle(awsResp)
        val resp = handle.getResult
        Xor.Right(resp)
      } else {
        response.headers.foreach {
          h ⇒ awsResp.addHeader(h.name, h.value)
        }
        Xor.Left(errorResponseHandler.handle(awsResp))
      }
    }
  }

  private[this] def signer(serviceName: String): Signer = {
    val s = new AWS4Signer(true)
    s.setServiceName(serviceName)
    s
  }

  private[this] def formData[T](awsReq: Request[T]) =
    HttpEntity(ContentType(MediaTypes.`application/x-www-form-urlencoded`, HttpCharsets.`ISO-8859-1`), encodeQuery(awsReq))

  private[this] def headers(req: Request[_]): List[HttpHeader] = {
    req.getHeaders.asScala.map {
      case (k: String, v: String) ⇒
        RawHeader(k, v)
    }.toList
  }

  private[this] def encodeQuery[T](awsReq: Request[T]) =
    awsReq.getParameters.asScala.toList.map({
      case (k, v) ⇒ s"${awsURLEncode(k)}=${awsURLEncode(v)}"
    }).mkString("&")

  private[this] def awsURLEncode(s: String) = Option(s).map(ss ⇒ URLEncoder.encode(ss, "UTF-8")).getOrElse("")

  import HttpMethods._
  implicit def bridgeMethods(m: HttpMethodName): HttpMethod = m match {
    case HttpMethodName.POST   ⇒ POST
    case HttpMethodName.GET    ⇒ GET
    case HttpMethodName.PUT    ⇒ PUT
    case HttpMethodName.DELETE ⇒ DELETE
    case HttpMethodName.HEAD   ⇒ HEAD
    case HttpMethodName.PATCH  ⇒ PATCH
  }
}
