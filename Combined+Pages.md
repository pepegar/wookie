

wookie
======

wookie is a library for using [Amazon Web Services](https://aws.amazon.com/es/)
services from Scala.


add to your project
-------------------

```
"io.pepegar.gihub" %% "wookie-core" % "0.1-SNAPSHOT"
```

Each one of the AWS Services are in its own isolated project, and they follow
the following naming structure:

```
"io.pepegar.gihub" %% "wookie-{service}" % "0.1-SNAPSHOT"
```

So, for example, if you want to use DynamoDB,

```
"io.pepegar.gihub" %% "wookie-dynamodb" % "0.1-SNAPSHOT"
```

Currently implemented services
------------------------------

Currently we have implementation for:

* DynamoDB
* S3


Rationale
=========

Purely functional AWS library
-----------------------------
Wookie is based in free monads and interpreters.  With this approach we can have
all the operations we want to have in an abstract syntax tree that will be
evaluated by our interpreter upon our request.

Modular
-------
Wookie lets you use the HTTP client of your choice. There are interpreters for
the following clients:

- [akka-http][akka-http]

But you can implement your own! You just need to implement a function like
follows:

```scala
def interpret[A](op: DynamoDBOp[A]): Future[A]
```

[akka-http]: https://akka.io



DynamoDB
========

Install
-------
In order to use DynamoDB, you need to add `wookie-dynamodb` artifact to your
`build.sbt` file.

```
"io.github.pepegar" %% "wookie-dynamodb" % "0.1-SNAPSHOT"
```

Use
---
You can use the `Ops` typeclass, located in `language` package.  It provides 
convenient functions to generate request values for DynamoDB.

```scala
import wookie.dynamodb.implicits._
import wookie.dynamodb.language.Ops
import wookie.dynamodb.DynamoDB
import wookie.service.Properties
import com.amazonaws.services.dynamodbv2.model.ListTablesRequest
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

val props = Properties("accessKey", "secretAccessKey")
implicit val system = ActorSystem("wookie")
implicit val mat = ActorMaterializer()

val listTables = Ops.listTables(new ListTablesRequest)

val result = DynamoDB(props).run(listTables)
```


Structure
========

We've tried to follow an homogeneous structure through all the project.

Services
--------
* Each service is a different artifact
* each one of them provide the following packages:
  * `ast` which contains the Algebraic Data Type of the service. this algebraic
  data type contains all operations that can be done to the given service.
  * `interpreter` an interpreter of the ADT.
  * `ServiceName` object.  Wires up all parts of the service into one small
  object that provides a `run` method.  This method transforms a `SercviceOp[A]`
  into the target `Monad`.
  * `language` where the typeclass of the service is defined and implemented.
  * `implicits` contains all the implicits related to the underlying `aws-java`
  project.  All our services calls need a `Marshaller[Request[A], A]`, where `A`
  is our request type, and a response handler.



DynamoDB
========

Install
-------
In order to use DynamoDB, you need to add `wookie-dynamodb` artifact to your
`build.sbt` file.

```
"io.github.pepegar" %% "wookie-dynamodb" % "0.1-SNAPSHOT"
```

Use
---
You can use the `Ops` typeclass, located in `language` package.  It provides 
convenient functions to generate request values for DynamoDB.

```scala
import wookie.dynamodb.implicits._
import wookie.dynamodb.language.Ops
import wookie.dynamodb.DynamoDB
import wookie.service.Properties
import com.amazonaws.services.dynamodbv2.model.ListTablesRequest
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

val props = Properties("accessKey", "secretAccessKey")
implicit val system = ActorSystem("wookie")
implicit val mat = ActorMaterializer()

val listTables = Ops.listTables(new ListTablesRequest)

val result = DynamoDB(props).run(listTables)
```
