package wookie
package s3

import cats.data.{ Xor, Kleisli }
import cats.~>

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import ast._
import result._
import marshaller._

object interpreter extends Interpreter {

  def s3Interpreter(endpoint: String)(
    implicit
    system: ActorSystem,
    mat: ActorMaterializer
  ) = new (S3Op ~> Result) {

    def apply[A](command: S3Op[A]): Result[A] =
      Kleisli { signer: Signer[A] =>
        send(endpoint, signer.sign(command.req))(command.responseHandler, system, mat)
      }

  }

}
