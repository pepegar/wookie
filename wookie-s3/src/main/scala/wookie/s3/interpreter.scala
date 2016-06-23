package wookie
package s3

import cats.data.{Xor, Kleisli}
import cats.~>

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import ast._
import service._
import marshaller._

import wookie.{interpreter => i}

object interpreter extends i.Interpreter {

  def s3Interpreter(endpoint: String)(
    implicit
    system: ActorSystem,
    mat: ActorMaterializer
  ) = new (S3Op ~> Result) {
  
    def apply[A](command: S3Op[A]): Result[A] = 
      Kleisli { marshaller: SignMarshaller[A] =>
        send(endpoint, marshaller.marshallAndSign(command.req, marshaller.credentials))(command.responseHandler, system, mat)
      }

  }

}
