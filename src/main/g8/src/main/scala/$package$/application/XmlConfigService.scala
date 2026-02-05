package $package$.application

import zio.ZIO

import $package$.infrastructure.XmlLoadService

object XmlConfigService {

  def importConfig(configXml: String): ZIO[Any, Throwable, String] =
    ZIO.attempt {
      val xml = XmlLoadService.loadXml(configXml)
      xml.text
    }
}
