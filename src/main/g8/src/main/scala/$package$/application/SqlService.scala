package $package$.application

import zio._
import zio.jdbc._

object SqlService {

  def runRawQuery(taintedSql: String): ZIO[ZConnection, Throwable, zio.Chunk[String]] = {
    //CWE-89
    //SINK
    SqlFragment(taintedSql).query[String].selectAll
  }
}
