package $package$.application

import play.twirl.api.Html

object HtmlRenderService {

  def renderAsHtml(content: String): Html = {
    //CWE-79
    //SINK
    new Html(content)
  }
}
