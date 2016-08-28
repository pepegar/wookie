package wookie

import cats.Monad
import cats.data.Kleisli

import scala.concurrent.Future

object result {
  import signer._

  type Result[A] = Kleisli[Future, Signer, A]

  implicit val resultMonad: Monad[Result] = implicitly
}

