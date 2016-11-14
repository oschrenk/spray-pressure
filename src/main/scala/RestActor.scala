import akka.actor.{Actor, ActorRefFactory}
import spray.routing.ExceptionHandler
import spray.routing._

class RestActor extends Actor with HttpService {

  def actorRefFactory = context

  implicit val exceptionHandler =
    ExceptionHandler {
      case t: Throwable => ctx => {
        ctx.complete(500, s"Error: [${t.getMessage}]")
      }
    }

  val detachedRoute = new DetachedRoute(context)

  def receive = runRoute(
    detachedRoute.route
  )
}
