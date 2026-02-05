package $package$.infrastructure

import net.liftweb.util.Helpers

object PasswordHashService {

  def hashPassword(password: String): String = {
    //CWE-328
    //SINK
    Helpers.md5(password)
  }
}
