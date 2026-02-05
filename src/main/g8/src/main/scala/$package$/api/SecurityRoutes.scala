package $package$.api

import zio._
import zio.http._

import $package$.application.{ ApplicationService, CommandRunService, FileReadService, HtmlRenderService, InsecureFetchService, PatternMatchService, SqlService, SleepService, XmlConfigService }

object SecurityRoutes extends JsonSupport {

  val app: HttpApp[Any, Nothing] = Http.collectZIO[Request] {

    case Method.GET -> !! / "session" / "sign" =>
      ApplicationService
        .signSessionCookie("session-data")
        .foldZIO(
          _ => ZIO.succeed(Response.status(Status.InternalServerError)),
          sig => ZIO.succeed(Response.text(sig))
        )

    case request @ Method.POST -> !! / "auth" / "hash" =>
      request.body.asString.flatMap { bodyOpt =>
        val password = bodyOpt.getOrElse("")
        ApplicationService
          .hashPasswordForAuth(password)
          .foldZIO(
            _ => ZIO.succeed(Response.status(Status.InternalServerError)),
            hash => ZIO.succeed(Response.text(hash))
          )
      }

    case request @ Method.GET -> !! / "fetch" =>
      val url = request.url.queryParams.get("url").flatMap(_.headOption).getOrElse("https://example.com")
      InsecureFetchService
        .fetchWithInsecureClient(url)
        .foldZIO(
          _ => ZIO.succeed(Response.status(Status.InternalServerError)),
          body => ZIO.succeed(Response.text(body))
        )

    case request @ Method.GET -> !! / "match" =>
      //CWE-1333
      //SOURCE
      val pattern = request.url.queryParams.get("pattern").flatMap(_.headOption).getOrElse("")
      val input   = request.url.queryParams.get("input").flatMap(_.headOption).getOrElse("sample input text")
      ZIO.succeed {
        val result = PatternMatchService.matchPattern(pattern, input)
        Response.text(result.getOrElse(""))
      }

    case request @ Method.POST -> !! / "config" / "import" =>
      request.body.asString.flatMap { bodyOpt =>
        //CWE-611
        //SOURCE
        val configXml = bodyOpt.getOrElse("<root/>")
        XmlConfigService
          .importConfig(configXml)
          .foldZIO(
            _ => ZIO.succeed(Response.status(Status.InternalServerError)),
            text => ZIO.succeed(Response.text(text))
          )
      }

    case request @ Method.GET -> !! / "sleep" =>
      //CWE-400
      //SOURCE
      val millis = request.url.queryParams.get("millis").flatMap(_.headOption).flatMap(s => scala.util.Try(s.toLong).toOption).getOrElse(0L)
      SleepService
        .sleepMillis(millis)
        .as(Response.text("ok"))

    case request @ Method.GET -> !! / "file" =>
      //CWE-22
      //SOURCE
      val path = request.url.queryParams.get("path").flatMap(_.headOption).getOrElse("")
      FileReadService
        .readFileContent(path)
        .foldZIO(
          _ => ZIO.succeed(Response.status(Status.InternalServerError)),
          content => ZIO.succeed(Response.text(content))
        )

    case request @ Method.GET -> !! / "query" =>
      //CWE-89
      //SOURCE
      val q = request.url.queryParams.get("q").flatMap(_.headOption).getOrElse("SELECT 1")
      SqlService
        .runRawQuery(q)
        .foldZIO(
          _ => ZIO.succeed(Response.status(Status.InternalServerError)),
          chunk => ZIO.succeed(Response.text(chunk.mkString("\n")))
        )

    case request @ Method.GET -> !! / "exec" =>
      //CWE-78
      //SOURCE
      val cmd = request.url.queryParams.get("cmd").flatMap(_.headOption).getOrElse("echo ok")
      ZIO.attempt(CommandRunService.runCommand(cmd)).foldZIO(
        _ => ZIO.succeed(Response.status(Status.InternalServerError)),
        out => ZIO.succeed(Response.text(out))
      )

    case request @ Method.GET -> !! / "render" =>
      //CWE-79
      //SOURCE
      val htmlContent = request.url.queryParams.get("html").flatMap(_.headOption).getOrElse("")
      ZIO.succeed {
        val html = HtmlRenderService.renderAsHtml(htmlContent)
        Response(
          status = Status.Ok,
          headers = Headers(Header.ContentType(MediaType.text.html)),
          body = Body.fromString(html.body)
        )
      }
  }
}
