package wookie
package akkahttp

import java.io.ByteArrayInputStream
import java.net.URLEncoder

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding._
import akka.http.scaladsl.model.MediaType.NotCompressible
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.stream.ActorMaterializer
import akka.util.{ ByteString, Timeout }
import cats.data.Xor
import com.amazonaws.auth._
import com.amazonaws.http.{ HttpMethodName, HttpResponseHandler, HttpResponse ⇒ AWSHttpResponse }
import com.amazonaws.{ AmazonServiceException, AmazonWebServiceResponse, DefaultRequest, Request }
import wookie.httpclient._

import scala.collection.JavaConverters._
import scala.concurrent.duration._
import scala.concurrent.{ ExecutionContext, Future }

object client {

  case class AkkaHttpClient(
      system: Option[ActorSystem]       = None,
      mat:    Option[ActorMaterializer] = None,
      ec:     Option[ExecutionContext]  = None
  ) extends HttpClient {

    implicit val s = system.getOrElse(ActorSystem("wookie"))
    implicit val m = mat.getOrElse(ActorMaterializer())
    implicit val e = ec.getOrElse(ExecutionContext.Implicits.global)

    def exec[A, B](request: Request[A])(
      implicit
      H:  HttpResponseHandler[AmazonWebServiceResponse[B]],
      EH: HttpResponseHandler[AmazonServiceException]
    ): Future[B] = {
      for {
        httpResponse ← sendRequest(createHttpRequest(request))
        marshalledResponse ← parseResponse(request.getServiceName, httpResponse)
      } yield marshalledResponse match {
        case Xor.Right(resp) ⇒ resp
        case Xor.Left(exc)   ⇒ throw exc
      }
    }

    val timeout: Timeout = Timeout(5 seconds)

    val defaultContentType = "application/x-amz-json-1.0"

    private[this] def createHttpRequest[T](request: Request[T]): HttpRequest = {
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

    private[this] def sendRequest(req: HttpRequest): Future[HttpResponse] = Http().singleRequest(req)

    private[this] def parseResponse[T](serviceName: String, response: HttpResponse)(
      implicit
      handler:              HttpResponseHandler[AmazonWebServiceResponse[T]],
      errorResponseHandler: HttpResponseHandler[AmazonServiceException]
    ): Future[AmazonServiceException Xor T] = {
      implicit val ec = s.dispatcher
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

}
