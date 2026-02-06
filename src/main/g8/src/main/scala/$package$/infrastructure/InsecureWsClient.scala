package $package$.infrastructure

import com.typesafe.config.ConfigFactory
import play.api.libs.ws.ahc.{ AhcWSClientConfigFactory, AhcWSClient }

object InsecureWsClient {

  def createClient(): AhcWSClient = {
    val base   = ConfigFactory.load()
    val loose = ConfigFactory.parseString("""
      play.ws.ssl.loose.acceptAnyCertificate = true
      play.ws.ssl.loose.disableHostnameVerification = true
    """)
    val config  = loose.withFallback(base)
    val wsConfig = AhcWSClientConfigFactory.forConfig(config, getClass.getClassLoader)
    //CWE-295
    //SINK
    new AhcWSClient(wsConfig)
  }
}
