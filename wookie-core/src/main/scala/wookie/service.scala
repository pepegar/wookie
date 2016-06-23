package wookie

import cats.Monad
import cats.data.Kleisli

import scala.concurrent.Future

object service {
  import marshaller._
  import Kleisli._

  type Result[A] = Kleisli[Future, SignMarshaller[A], A]

  implicit val resultMonad: Monad[Result] = implicitly

  trait Service {
    def endpoint: String
  }
}
