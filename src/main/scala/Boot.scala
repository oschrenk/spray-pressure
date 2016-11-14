import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http

import scala.concurrent.ExecutionContext.Implicits.global

object Boot extends App {
    implicit val system = ActorSystem("spray-pressure")

    val service = system.actorOf(Props(classOf[RestActor]), "rest-api")

    IO(Http) ! Http.Bind(service, interface = "127.0.0.1", port = 8080)
}
