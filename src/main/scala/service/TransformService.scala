package service

import akka.actor.ActorSystem
import network.{AsyncClient, BlockingClient}
import org.slf4j.LoggerFactory

import scala.concurrent.Future

class TransformService(asyncClient: AsyncClient, blockingClient: BlockingClient)(implicit system: ActorSystem) {

  val logger = LoggerFactory.getLogger(classOf[AsyncClient])

  private implicit val ec = system.dispatchers.lookup("execution-contexts.service")

  def snake(one: Option[String]): String = {
    one match {
      case None => "sad snake"
      case Some(thing) => s"ssssss $thing ssssss"
    }
  }

  def fetch(text: String, millis: Long): Future[String] = {
    for {
     thing <- asyncClient.fetch(text, millis)
     _ = logger.info("inside for")
    } yield {
      logger.info("inside yield")
      snake(thing)
    }
  }

  def block(text: String, millis: Long): String = {
    snake(blockingClient.block(text, millis))
  }

}
