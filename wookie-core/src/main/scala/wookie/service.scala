package wookie

object service {
  trait Service {
    def endpoint: String
    def serviceName: String
  }

  case class Properties(
    accessKey: String,
    secretAccessKey: String
  )
}
