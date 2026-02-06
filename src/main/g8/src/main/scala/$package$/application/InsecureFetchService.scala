package $package$.application

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import zio.ZIO

import $package$.infrastructure.InsecureWsClient

object InsecureFetchService {

  def fetchWithInsecureClient(url: String): ZIO[Any, Throwable, String] =
    ZIO.attempt {
      val client = InsecureWsClient.createClient()
      try {
        val resp = Await.result(client.url(url).get(), Duration.Inf)
        resp.body
      } finally client.close()
    }
}
