package routes

import akka.actor.ActorSystem
import akka.event.Logging
import org.slf4j.LoggerFactory
import spray.routing.HttpService
import spray.routing.directives.DebuggingDirectives

class DetachedRoute(implicit system: ActorSystem) extends HttpService {
  val logDirective = DebuggingDirectives.logRequestResponse("get-detached", Logging.InfoLevel)
  val logger = LoggerFactory.getLogger(classOf[DetachedRoute])

  def actorRefFactory = system

  private val ecDetach = system.dispatchers.lookup("execution-contexts.detach")

  val route = pathPrefix("detach") {
    get {
      path("none") {
        logDirective {
          complete {
            logger.info("Inside un-detached complete")
            "Not detached"
          }
        }
      } ~ path("implicit") {
        detach() {
          logDirective {
            complete {
              logger.info("Inside implicit detach complete")
              "Implicitly detached"
            }
          }
        }
      } ~ path("explicit") {
        detach(ecDetach) {
          logDirective {
            complete {
              logger.info("Inside explicit detach complete")
              "Explicitly detached"
            }
          }
        }
      }
    }
  }
}
