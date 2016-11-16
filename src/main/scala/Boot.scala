import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http

import scala.concurrent.ExecutionContext.Implicits.global

object Boot extends App {
    implicit val system = ActorSystem("spray-pressure")

    val serviceCustom = system.actorOf(Props(classOf[RestActor]).withDispatcher("custom-dispatcher"),"rest-api-custom")

    IO(Http) ! Http.Bind(serviceCustom, interface = "127.0.0.1", port = 8081)
}
