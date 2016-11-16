package network

import org.slf4j.LoggerFactory

class BlockingClient {

  val logger = LoggerFactory.getLogger(classOf[AsyncClient])

  def block(text: String, millis: Long): Option[String] = {
    logger.info(s"sleeping for $millis milliseconds")
    Thread.sleep(millis)
    logger.info("done sleeping")
    Some(text)
  }
}
