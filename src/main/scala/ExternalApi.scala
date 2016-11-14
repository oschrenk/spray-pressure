import spray.routing.HttpService
import scala.concurrent.Future

import akka.actor.{ActorSystem, Props, ActorRefFactory}
import spray.httpx.SprayJsonSupport
import spray.json._

case class Foo(bar: String)

class ExternalApi(refFactory: ActorRefFactory) extends HttpService with DefaultJsonProtocol with SprayJsonSupport {
  override implicit def actorRefFactory: ActorRefFactory = refFactory
  implicit val FooFormat = jsonFormat1(Foo)

  val detachedRoute = pathPrefix("api" / "detached") {
    detach() {
      get {
        complete("")
      }
    }
  }
}
