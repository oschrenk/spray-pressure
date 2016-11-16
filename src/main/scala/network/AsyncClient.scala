package network

import akka.actor.ActorSystem
import org.slf4j.LoggerFactory
import spray.client.pipelining._
import spray.http.{HttpRequest, HttpResponse}

import scala.concurrent.Future

class AsyncClient(implicit system: ActorSystem) {

  val logger = LoggerFactory.getLogger(classOf[AsyncClient])

  private implicit val ec = system.dispatchers.lookup("execution-contexts.network")

  val logRequest: HttpRequest => HttpRequest = {
    r => logger.info(s"request uri: ${r.uri.toString()}"); r
  }
  val logResponse: HttpResponse => HttpResponse = {
    r => logger.info(s"response: ${r.toString}"); r
  }

  val pipeline: HttpRequest => Future[HttpResponse] = logRequest ~> sendReceive ~> logResponse

  def fetch(text: String, millis: Long): Future[Option[String]] = {
    val uri = "http://www.google.com"
    logger.info(s"sleeping for $millis milliseconds")
    Thread.sleep(millis)
    logger.info("before pipeline")
    pipeline(Get(uri)).map {
      response => response.status match {
        case _ =>
          logger.info("response handling")
          Some(text)
      }
    }
  }
}
