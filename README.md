wookie
======

[![Build Status](https://travis-ci.org/pepegar/wookie.svg?branch=master)](https://travis-ci.org/pepegar/wookie)

<img alt="GHHHAAAAAAHAHHAHAAHAAAAAA" align=right src="chewbacca.jpg"/>

**wookie** is a purely functional library for connecting to AWS with Scala.

### [Documentation][docs]

**wookie** is created to be:

* _Pure functional_.  **wookie** is based on [Free monads][free monads].  It
  provides interpreters to evaluate these monadic computations and communicate
  with AWS servers.
* _Modular_.  Each service lives in its own package, and they share only a few
  abstractions.  Also, each http client lives in its own package, and you can
  import the one that fits for your project, or [implement your own][clients].

### Quickstart
In order for you to start coding, you will need to import latest service project
in your SBT `build.sbt` file.

```
libraryDependencies += "io.github.pepegar" %% "wookie-dynamodb" % "0.1-SNAPSHOT"
libraryDependencies += "io.github.pepegar" %% "wookie-s3" % "0.1-SNAPSHOT"
```

And you might want to pull a client as well:

```
libraryDependencies += "io.github.pepegar" %% "wookie-akka-http" % "0.1-SNAPSHOT"
```

### Inspiration

**wookie** is inspired in several projects out there.

* [doobie][doobie] principled DB access for Scala.
* [fetch][fetch] efficient data access for Scala.

[docs]: https://pepegar.github.io/wookie
[free monads]: http://typelevel.org/cats/tut/freemonad.html
[clients]: https://pepegar.github.io/wookie/structure.html#HTTP+Clients
[doobie]: https://github.com/tpolecat/doobie
[fetch]: https://github.com/47deg/fetch
