package $package$.application

import zio._
import zio.duration.Duration

object SleepService {

  def sleepMillis(millis: Long): UIO[Unit] = {
    //CWE-400
    //SINK
    val duration = Duration.fromMillis(millis)
    ZIO.sleep(duration)
  }
}
