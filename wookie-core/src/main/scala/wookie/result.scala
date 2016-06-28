package wookie

import cats.Monad
import cats.data.Kleisli

import scala.concurrent.Future

object result {
  import signer._
  import Kleisli._

  type Result[A] = Kleisli[Future, Signer[A], A]

  implicit val resultMonad: Monad[Result] = implicitly
}

