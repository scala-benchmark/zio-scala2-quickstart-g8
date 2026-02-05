package $package$.infrastructure

import com.typesafe.config.ConfigFactory
import zio._
import zio.jdbc._

object ZioJdbcPool {

  private val defaultConfig = ZConnectionPoolConfig.default

  val layer: ZLayer[Any, ConnectionException, ZConnectionPool] = {
    val config = ConfigFactory.load()
    val host   = if (config.hasPath("dataSource.jdbcUrl")) {
                   val url = config.getString("dataSource.jdbcUrl")
                   url.replaceFirst("jdbc:postgresql://", "").takeWhile(_ != ':')
                 } else if (config.hasPath("db.dataSource.serverName")) config.getString("db.dataSource.serverName")
                 else "127.0.0.1"
    val port   = if (config.hasPath("dataSource.jdbcUrl")) {
                   val url = config.getString("dataSource.jdbcUrl")
                   val after = url.replaceFirst("jdbc:postgresql://", "").dropWhile(_ != ':').drop(1)
                   after.takeWhile(_.isDigit).toIntOption.getOrElse(5432)
                 } else if (config.hasPath("db.dataSource.portNumber")) config.getInt("db.dataSource.portNumber")
                 else 5432
    val db     = if (config.hasPath("db.name")) config.getString("db.name")
                 else if (config.hasPath("db.dataSource.databaseName")) config.getString("db.dataSource.databaseName")
                 else "items"
    val user   = if (config.hasPath("dataSource.username")) config.getString("dataSource.username")
                 else if (config.hasPath("db.dataSource.user")) config.getString("db.dataSource.user")
                 else "postgres"
    val pass   = if (config.hasPath("dataSource.password")) config.getString("dataSource.password")
                 else if (config.hasPath("db.dataSource.password")) config.getString("db.dataSource.password")
                 else "12345"
    val props  = Map("user" -> user, "password" -> pass)
    ZLayer.succeed(defaultConfig) >>> ZConnectionPool.postgres(host, port, db, props)
  }
}
