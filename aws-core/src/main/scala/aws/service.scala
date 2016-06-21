package aws

object service {
  trait Service {
    def endpoint: String
  }
}
