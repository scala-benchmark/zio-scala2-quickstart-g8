package $package$.application

import play.twirl.api.Html

object HtmlRenderService {

  def renderAsHtml(content: String): Html = {


    new Html(content)
  }
}
