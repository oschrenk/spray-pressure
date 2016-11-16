package routes

import akka.actor.ActorSystem
import akka.event.Logging
import org.slf4j.LoggerFactory
import service.TransformService
import spray.routing.HttpService
import spray.routing.directives.DebuggingDirectives

class TransformRoute(transformService: TransformService)(implicit system: ActorSystem) extends HttpService {
  val logDirective = DebuggingDirectives.logRequestResponse("get-detached", Logging.InfoLevel)
  val logger = LoggerFactory.getLogger(classOf[DetachedRoute])

  def actorRefFactory = system

  // need to bring this in for future marshalling/handling
  private implicit def ec = actorRefFactory.dispatcher

  private val ecDetach = system.dispatchers.lookup("execution-contexts.detach")

  val route = pathPrefix("fetch") {
    get {
      path("normal") {
        logDirective {
          complete {
            logger.info("Inside un-detached complete")
            transformService.fetch("fetch", 1000L)
          }
        }
      } ~ path("detach-implicit") {
        detach() {
          logDirective {
            complete {
              logger.info("Inside implicit detach complete")
              transformService.fetch("fetch", 1000L)
            }
          }
        }
      } ~ path("detach-explicit") {
        detach(ecDetach) {
          logDirective {
            complete {
              logger.info("Inside explicit detach complete")
              transformService.fetch("fetch", 1000L)
            }
          }
        }
      }
    }
  }
}
