import akka.actor.Actor
import network.{AsyncClient, BlockingClient}
import routes.{DetachedRoute, TransformRoute}
import service.TransformService
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

  val asyncClient = new AsyncClient()
  val blockingClient = new BlockingClient()
  val transformService = new TransformService(asyncClient, blockingClient)

  val detachedRoute = new DetachedRoute()(context.system)
  val transformRoute = new TransformRoute(transformService)

  def receive = runRoute(
    detachedRoute.route ~ transformRoute.route
  )
}
