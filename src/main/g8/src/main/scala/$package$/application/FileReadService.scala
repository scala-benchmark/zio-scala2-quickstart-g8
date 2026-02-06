package $package$.application

import better.files._
import zio._

object FileReadService {

  def readFileContent(filePath: String): ZIO[Any, Throwable, String] =
    ZIO.attempt {
      val f = File(filePath)
      //CWE-22
      //SINK
      f.contentAsString
    }
}
