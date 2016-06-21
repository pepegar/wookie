package aws
package dynamodb

import cats.Monad
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object implicits {

  implicit val futureMonad = new Monad[Future] {
    def pure[A](x: A): Future[A] = Future.successful(x)
    def flatMap[A, B](fa: Future[A])(f: A => Future[B]): Future[B] =
      fa flatMap f
  }

}
