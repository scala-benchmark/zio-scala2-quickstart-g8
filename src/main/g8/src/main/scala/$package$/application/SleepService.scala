package $package$.application

import zio._
import zio.duration.Duration

object SleepService {

  def sleepMillis(millis: Long): UIO[Unit] = {
    val duration = Duration.fromMillis(millis)
    //CWE-400
    //SINK
    ZIO.sleep(duration)
  }
}
