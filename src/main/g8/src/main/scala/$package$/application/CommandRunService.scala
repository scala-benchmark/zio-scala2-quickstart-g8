package $package$.application

import scala.sys.process._

object CommandRunService {

  def runCommand(cmd: String): String = {
    //CWE-78
    //SINK
    cmd.!!
  }
}
