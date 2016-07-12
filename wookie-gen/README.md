Code Generation sub-project
---------------------------

Code generation is a experimenta feature we are testing.  It consists on
generating the whole algebra & typeclass of a service on the fly given a list of
request and result classes.  It has it flaws but provides us several good
points:

1. It makes our services shape more consistent.
2. It makes much easier the development of services.

Generating new services
-----------------------

To generate new services, please generate the sub-project first, and make it
depend on [core project][core] in [build.sbt][build].

After that, you will want to generate the algebra & typeclass for your service.
You can do it in the following way:

1. Add your service to [`CodeGen`][codegen], in the `App` object.
2. Generate via `sbt "gen/run yourservice"`

this will generate an `algebra.scala` file in your service's source directory.


[core]: https://github.com/pepegar/wookie/blob/master/wookie-core
[codegen]: https://github.com/pepegar/wookie/blob/master/wookie-gen/src/main/scala/wookie/gen/CodeGen.scala
[build]: https://github.com/pepegar/wookie/blob/master/build.sbt
