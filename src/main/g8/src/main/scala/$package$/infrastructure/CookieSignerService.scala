package $package$.infrastructure

import play.api.http.SecretConfiguration
import play.api.libs.crypto.DefaultCookieSigner

object CookieSignerService {

  private val secretConfig = SecretConfiguration("cookie-signer-secret")
  private val signer = new DefaultCookieSigner(secretConfig)

  def defaultKey: Array[Byte] = "cookie-signer-secret-key".getBytes(java.nio.charset.StandardCharsets.UTF_8)

  def sign(message: String, key: Array[Byte]): String =
    signer.sign(message, key)
}
