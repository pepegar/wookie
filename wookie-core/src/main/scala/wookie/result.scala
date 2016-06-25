package wookie

import cats.Monad
import cats.data.Kleisli

import scala.concurrent.Future

object result {
  import marshaller._
  import Kleisli._

  type Result[A] = Kleisli[Future, SignMarshaller[A], A]

  implicit val resultMonad: Monad[Result] = implicitly
}

