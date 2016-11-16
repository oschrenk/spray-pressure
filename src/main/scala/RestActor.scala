import akka.actor.Actor
import routes.DetachedRoute
import spray.routing.ExceptionHandler
import spray.routing._

class RestActor extends Actor with HttpService {

  def actorRefFactory = context
  implicit def system = context.system

  implicit val exceptionHandler =
    ExceptionHandler {
      case t: Throwable => ctx => {
        ctx.complete(500, s"Error: [${t.getMessage}]")
      }
    }

  val detachedRoute = new DetachedRoute()(context.system)

  def receive = runRoute(
    detachedRoute.route
  )
}
