package service

import akka.actor.ActorSystem
import network.{AsyncClient, BlockingClient}

import scala.concurrent.Future

class TransformService(asyncClient: AsyncClient, blockingClient: BlockingClient)(implicit system: ActorSystem) {

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
    } yield {
      snake(thing)
    }
  }

  def block(text: String, millis: Long): String = {
    snake(blockingClient.block(text, millis))
  }

}
